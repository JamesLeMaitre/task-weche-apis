package dev.perogroupe.wecheapis.services.impls

import dev.perogroupe.wecheapis.dtos.requests.CheckRequestStatusReq
import dev.perogroupe.wecheapis.dtos.requests.RejectedRequestReq
import dev.perogroupe.wecheapis.dtos.responses.RejectedRequestResponse
import dev.perogroupe.wecheapis.entities.Notifications
import dev.perogroupe.wecheapis.events.CheckRequestStatusEvent
import dev.perogroupe.wecheapis.exceptions.PendingRequestNotFoundException
import dev.perogroupe.wecheapis.exceptions.RejectedRequestNotFoundException
import dev.perogroupe.wecheapis.exceptions.UserNotFoundException
import dev.perogroupe.wecheapis.repositories.NewRequestRepository
import dev.perogroupe.wecheapis.repositories.PendingRequestRepository
import dev.perogroupe.wecheapis.repositories.RejectedRequestRepository
import dev.perogroupe.wecheapis.repositories.UserRepository
import dev.perogroupe.wecheapis.services.NotificationsService
import dev.perogroupe.wecheapis.services.RejectRequestService
import dev.perogroupe.wecheapis.utils.enums.RequestStatus
import dev.perogroupe.wecheapis.utils.response
import dev.perogroupe.wecheapis.utils.toRejectedRequest
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class RejectRequestServiceImpl(
    private val newRequestRepository: NewRequestRepository,
    private val eventPublisher: ApplicationEventPublisher,
    private val repository: RejectedRequestRepository,
    private val pendingRequestRepository: PendingRequestRepository,
    private val userRepository: UserRepository,
    private val notificationsService: NotificationsService
) :RejectRequestService{
    override fun store(request: RejectedRequestReq): RejectedRequestResponse = newRequestRepository
        .findByRequestNumber(request.requestId)
        .map { newRequestRepository.save(it.copy(requestStatus = RequestStatus.DONE)) }
        .map {
            val req = CheckRequestStatusReq(
                requestNumber = it.requestNumber!!,
                requestStatus = RequestStatus.REJECTED,
                user = it.user!!,
                comment = request.reason,
                createdBySupAt = Instant.now()
            )
            eventPublisher.publishEvent(CheckRequestStatusEvent(this, req))
            val notification = Notifications(
                user = it.user,
                message = "Demande ${it.requestNumber} a été rejetée.",
                read = false
            )
            notificationsService.store(notification)
            repository.save(it.toRejectedRequest().copy(rejectReason = request.reason))
        }.orElseThrow { RejectedRequestNotFoundException("New request with id ${request.requestId} not found") }
        .response()

    override fun storeForSupAdmin(request: RejectedRequestReq): RejectedRequestResponse = pendingRequestRepository
        .findByRequestNumber(request.requestId)
        .map { pendingRequestRepository.save(it.copy(requestStatus = RequestStatus.DONE)) }
        .map {
            val req = CheckRequestStatusReq(
                requestNumber = it.requestNumber!!,
                requestStatus = RequestStatus.REJECTED,
                user = it.user!!,
                comment = request.reason,
                createByDpafAt = Instant.now()
            )
            eventPublisher.publishEvent(CheckRequestStatusEvent(this, req))
            val notification = Notifications(
                user = it.user,
                message = "Demande ${it.requestNumber} a été rejetée.",
                read = false
            )
            notificationsService.store(notification)
            eventPublisher.publishEvent(CheckRequestStatusEvent(this, req))
            repository.save(it.toRejectedRequest().copy(rejectReason = request.reason))
        }.orElseThrow { PendingRequestNotFoundException("Pending request with id ${request.requestId} not found") }
        .response()

    override fun list(): List<RejectedRequestResponse> =
        repository.findAll(Sort.by(Sort.Direction.ASC, "created_at")).map { it.response() }

    override fun list(
        page: Int,
        size: Int,
    ): Page<RejectedRequestResponse> = repository
        .findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "created_at")))
        .map { it.response() }

    override fun show(requestNumber: String): RejectedRequestResponse = repository
        .findByRequestNumber(requestNumber)
        .map { it.response() }
        .orElseThrow { RejectedRequestNotFoundException("Rejected request with id $requestNumber not found") }

    override fun count(authentication: Authentication): Long {
        val user = userRepository
            .findByUsername(authentication.name)
            .orElseThrow { UserNotFoundException("User with this serial number ${authentication.name} not found") }
        return repository.countByStructureAndRequestStatusIsNot(user.structure, RequestStatus.DONE)
    }
}