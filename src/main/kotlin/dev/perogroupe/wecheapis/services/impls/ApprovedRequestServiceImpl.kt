package dev.perogroupe.wecheapis.services.impls

import dev.perogroupe.wecheapis.dtos.requests.CheckRequestStatusReq
import dev.perogroupe.wecheapis.dtos.responses.ApprovedRequestResponse
import dev.perogroupe.wecheapis.entities.Notifications
import dev.perogroupe.wecheapis.events.CheckRequestStatusEvent
import dev.perogroupe.wecheapis.exceptions.ApprovedRequestNotFoundException
import dev.perogroupe.wecheapis.exceptions.UserNotFoundException
import dev.perogroupe.wecheapis.repositories.ApprovedRequestRepository
import dev.perogroupe.wecheapis.repositories.PendingRequestRepository
import dev.perogroupe.wecheapis.repositories.UserRepository
import dev.perogroupe.wecheapis.services.ApprovedRequestService
import dev.perogroupe.wecheapis.services.NotificationsService
import dev.perogroupe.wecheapis.utils.enums.RequestStatus
import dev.perogroupe.wecheapis.utils.response
import dev.perogroupe.wecheapis.utils.toApprovedRequest
import dev.perogroupe.wecheapis.ws.services.WSService
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.domain.Page
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class ApprovedRequestServiceImpl(
    private val repository: ApprovedRequestRepository,
    private val pendingRequestRepository: PendingRequestRepository,
    private val wsService: WSService,
    private val eventPublisher: ApplicationEventPublisher,
    private val userRepository: UserRepository,
    private val notificationsService: NotificationsService,
) : ApprovedRequestService {
    /**
     * Store the approved request based on the provided ID.
     *
     * @param id The ID of the pending request to be approved.
     * @return The response indicating the success of the approval.
     * @throws ApprovedRequestNotFoundException If the pending request is not found.
     */
    override fun store(id: String): ApprovedRequestResponse = pendingRequestRepository
        .findById(id)
        .map { pendingRequestRepository.save(it.copy(requestStatus = RequestStatus.DONE)) }
        .map {
            // Create a CheckRequestStatusReq object for the approved request
            val req = CheckRequestStatusReq(
                requestNumber = it.requestNumber!!,
                requestStatus = RequestStatus.APPROVED,
                user = it.user!!,
                comment = "Approved",
                createByDpafAt = Instant.now()
            )

            // Send a message via WebSocket service
            wsService.sendMessage("Demande ${it.requestNumber} a été validée.")

            // Create a notification for the approved request
            val notification = Notifications(
                user = it.user,
                message = "Demande ${it.requestNumber} a été validée.",
                read = false
            )

            // Store the notification
            notificationsService.store(notification)

            // Publish an event for the approved request
            eventPublisher.publishEvent(CheckRequestStatusEvent(this, req))

            it
        }
        .orElseThrow { ApprovedRequestNotFoundException("Pending request not found") }
        .toApprovedRequest()
        .let {
            // Save the approved request to the repository and return the response
            val saved = repository.save(it)
            saved.response()
        }



    /**
     * Counts the number of non-rejected requests for a given user.
     *
     * @param authentication The authentication object containing the user's serial number.
     * @return The count of non-rejected requests for the user.
     * @throws UserNotFoundException If the user with the given serial number is not found.
     */
    override fun count(authentication: Authentication): Long {
        // Find the user by their serial number
        val user = userRepository
            .findByUsername(authentication.name)
            .orElseThrow { UserNotFoundException("User with this serial number ${authentication.name} not found") }

        // Count the number of non-rejected requests for the user's structure
        return repository.countByStructureAndRequestStatusIsNot(user.structure, RequestStatus.REJECTED)
    }
}