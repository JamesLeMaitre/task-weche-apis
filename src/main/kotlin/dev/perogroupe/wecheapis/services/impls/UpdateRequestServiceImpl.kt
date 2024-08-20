package dev.perogroupe.wecheapis.services.impls

import dev.perogroupe.wecheapis.dtos.requests.CheckRequestStatusReq
import dev.perogroupe.wecheapis.dtos.requests.UpRequest
import dev.perogroupe.wecheapis.dtos.responses.UpdateRequestResponse
import dev.perogroupe.wecheapis.entities.Notifications
import dev.perogroupe.wecheapis.events.CheckRequestStatusEvent
import dev.perogroupe.wecheapis.exceptions.PendingRequestNotFoundException
import dev.perogroupe.wecheapis.repositories.PendingRequestRepository
import dev.perogroupe.wecheapis.repositories.UpdateRequestRepository
import dev.perogroupe.wecheapis.services.NotificationsService
import dev.perogroupe.wecheapis.services.PendingRequestService
import dev.perogroupe.wecheapis.services.UpdateRequestService
import dev.perogroupe.wecheapis.utils.enums.RequestStatus
import dev.perogroupe.wecheapis.utils.response
import dev.perogroupe.wecheapis.utils.toUpdateRequest
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class UpdateRequestServiceImpl(
    private val repository: UpdateRequestRepository,
    private val pendingReqService: PendingRequestService,
    private val eventPublisher: ApplicationEventPublisher,
    private val pendingRepository: PendingRequestRepository,
    private val notificationsService: NotificationsService
) : UpdateRequestService {
    override fun store(request: UpRequest): UpdateRequestResponse {
        val req = request.toUpdateRequest()
        val pendingReq = pendingReqService.updateRequest(request.requestId)
        val checkStatus = CheckRequestStatusReq(
            requestNumber = pendingReq.requestNumber!!,
            requestStatus = RequestStatus.UPDATE,
            user = pendingReq.user!!,
            comment = request.reason,
            createByDpafAt = Instant.now()
        )
        req.request = pendingReq
        val notification = Notifications(
            user = pendingReq.user,
            message = "Demande ${pendingReq.requestNumber} est à modifier.",
            read = false
        )
        notificationsService.store(notification)
        eventPublisher.publishEvent(CheckRequestStatusEvent(this, checkStatus))
        return repository.save(req).response()
    }

    override fun show(requestNumber: String): UpdateRequestResponse  {
        val pendRes = pendingRepository.findByRequestNumber(requestNumber)
            .orElseThrow { PendingRequestNotFoundException("Request Not found") }
        return repository.findByRequest(pendRes)
            .map { it.response() }
            .orElseThrow { PendingRequestNotFoundException("Request not found") }

    }
}