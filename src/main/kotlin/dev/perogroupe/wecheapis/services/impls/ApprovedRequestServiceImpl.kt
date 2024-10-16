package dev.perogroupe.wecheapis.services.impls

import dev.perogroupe.wecheapis.dtos.requests.CheckRequestStatusReq
import dev.perogroupe.wecheapis.dtos.requests.CheckValidityRequest
import dev.perogroupe.wecheapis.dtos.requests.DnrRequest
import dev.perogroupe.wecheapis.dtos.responses.ApprovedRequestResponse
import dev.perogroupe.wecheapis.entities.Notifications
import dev.perogroupe.wecheapis.events.CheckRequestStatusEvent
import dev.perogroupe.wecheapis.exceptions.ApprovedRequestNotFoundException
import dev.perogroupe.wecheapis.exceptions.UserNotFoundException
import dev.perogroupe.wecheapis.repositories.ApprovedRequestRepository
import dev.perogroupe.wecheapis.repositories.NewRequestRepository
import dev.perogroupe.wecheapis.repositories.PendingRequestRepository
import dev.perogroupe.wecheapis.repositories.UserRepository
import dev.perogroupe.wecheapis.services.*
import dev.perogroupe.wecheapis.utils.enums.RequestStatus
import dev.perogroupe.wecheapis.utils.response
import dev.perogroupe.wecheapis.utils.toApprovedRequest
import dev.perogroupe.wecheapis.ws.services.WSService
import org.springframework.context.ApplicationEventPublisher
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*

@Service
class ApprovedRequestServiceImpl(
    private val repository: ApprovedRequestRepository,
    private val pendingRequestRepository: PendingRequestRepository,
    private val wsService: WSService,
    private val eventPublisher: ApplicationEventPublisher,
    private val userRepository: UserRepository,
    private val notificationsService: NotificationsService,
    private val newRequestRepository: NewRequestRepository,
    private val messageService: MessageService,
    private val dnrService: DnrService,
    private val checkValidityService: CheckValidityService,
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

            // Write the DNR
            dnrService.createDnr(DnrRequest(it.user, Date(), true, it.requestNumber))
            // Send a message via WebSocket service
            wsService.sendMessage("Demande N° ${it.requestNumber} a été validée.")

            // Create a notification for the approved request
            val notification = Notifications(
                user = it.user,
                message = "Demande N° ${it.requestNumber} a été validée.",
                read = false
            )

            // Store the notification
            notificationsService.store(notification)

            // Create a check validity
            val checkValidity = CheckValidityRequest(
                user = it.user,
                appDocument = true,
                appDnr = false,
                appDocumentDateDelivery = Instant.now(),
                appDnrDateDelivery = Instant.now()
            )
            checkValidityService.store(checkValidity)

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

    override fun create(id: String): ApprovedRequestResponse = newRequestRepository
        .findById(id)
        .map { newRequestRepository.save(it.copy(requestStatus = RequestStatus.DONE)) }
        .map {
            // Create a CheckRequestStatusReq object for the approved request
            val req = CheckRequestStatusReq(
                requestNumber = it.requestNumber!!,
                requestStatus = RequestStatus.APPROVED,
                user = it.user!!,
                comment = "Approved",
                createByDpafAt = Instant.now()
            )

            // Create a check validity
            val checkValidity = CheckValidityRequest(
                user = it.user,
                appDocument = true,
                appDnr = false,
                appDocumentDateDelivery = Instant.now(),
                appDnrDateDelivery = Instant.now()
            )
            checkValidityService.store(checkValidity)

            // Write the DNR
            dnrService.createDnr(DnrRequest(it.user, Date(), true, it.requestNumber))

            // Send a message via WebSocket service
            wsService.sendMessage("Demande N° ${it.requestNumber} a été validée.")

            // Create a notification for the approved request
            val notification = Notifications(
                user = it.user,
                message = "Demande N° ${it.requestNumber} a été validée.",
                read = false
            )



            // Store the notification
            notificationsService.store(notification)
            // Send email to user
            messageService.sendMail(
                it.user.email,
                "Demande d'attestation de présence validée",
                """
    Bonjour ${it.user.firstname},

    Nous avons le plaisir de vous informer que votre demande d'attestation de présence au poste a été validée.

    Veuillez vous connecter à la plateforme pour télécharger le document.

    Cordialement,

    L'équipe Weche
    """.trimIndent()
            )

            // Publish an event for the approved request
            eventPublisher.publishEvent(CheckRequestStatusEvent(this, req))

            it
        }
        .orElseThrow { ApprovedRequestNotFoundException("New request not found") }
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