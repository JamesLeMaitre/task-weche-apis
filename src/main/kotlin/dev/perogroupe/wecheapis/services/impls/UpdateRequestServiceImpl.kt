package dev.perogroupe.wecheapis.services.impls

import dev.perogroupe.wecheapis.dtos.requests.CheckRequestStatusReq
import dev.perogroupe.wecheapis.dtos.requests.UpRequest
import dev.perogroupe.wecheapis.dtos.responses.UpdateRequestResponse
import dev.perogroupe.wecheapis.entities.Notifications
import dev.perogroupe.wecheapis.events.CheckRequestStatusEvent
import dev.perogroupe.wecheapis.exceptions.PendingRequestNotFoundException
import dev.perogroupe.wecheapis.repositories.NewRequestRepository
import dev.perogroupe.wecheapis.repositories.PendingRequestRepository
import dev.perogroupe.wecheapis.repositories.UpdateRequestRepository
import dev.perogroupe.wecheapis.services.*
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
    private val newRequestService: NewRequestService,
    private val eventPublisher: ApplicationEventPublisher,
    private val pendingRepository: PendingRequestRepository,
    private val newRequestRepository: NewRequestRepository,
    private val notificationsService: NotificationsService,
    private val messageService: MessageService,
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
            message = "Demande N° ${pendingReq.requestNumber} est à modifier.",
            read = false
        )
        notificationsService.store(notification)
        eventPublisher.publishEvent(CheckRequestStatusEvent(this, checkStatus))
        return repository.save(req).response()
    }

    override fun create(request: UpRequest): UpdateRequestResponse {
        val req = request.toUpdateRequest()
        val newRequest = newRequestService.showByRequest(request.requestId)
        val checkStatus = CheckRequestStatusReq(
            requestNumber = newRequest.requestNumber!!,
            requestStatus = RequestStatus.UPDATE,
            user = newRequest.user!!,
            comment = request.reason,
            createByDpafAt = Instant.now()
        )
        req.newRequest = newRequest
        val notification = Notifications(
            user = newRequest.user,
            message = "Demande N° ${newRequest.requestNumber} est à modifier.",
            read = false
        )
        // Store the notification
        notificationsService.store(notification)

        //Send email to user
        messageService.sendMail(
            newRequest.user.email,
            "Demande d'attestation de présence à modifier",
            """
    Bonjour ${newRequest.user.firstname},

    Votre demande d'attestation de présence au poste a été rejetée pour la raison suivante :
    ${request.reason}

    Veuillez vous connecter à la plateforme pour modifier votre demande.

    Cordialement,

    L'équipe Weche
    """.trimIndent()
        )



        // Publish an event for the check request status
        eventPublisher.publishEvent(CheckRequestStatusEvent(this, checkStatus))
        return repository.save(req).response()
    }

    override fun show(requestNumber: String): UpdateRequestResponse  {
        val newReq = newRequestRepository.findByRequestNumber(requestNumber)
            .orElseThrow { PendingRequestNotFoundException("New Request Not found") }
        val list = repository.findAllByNewRequest(newReq)
        // return the last one and delete the others
        return list.last().response()
    }
}