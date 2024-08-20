package dev.perogroupe.wecheapis.services.impls

import dev.perogroupe.wecheapis.dtos.requests.CheckRequestStatusReq
import dev.perogroupe.wecheapis.dtos.requests.UpdateRequestReq
import dev.perogroupe.wecheapis.dtos.responses.PendingRequestResponse
import dev.perogroupe.wecheapis.entities.Notifications
import dev.perogroupe.wecheapis.entities.PendingRequest
import dev.perogroupe.wecheapis.events.CheckRequestStatusEvent
import dev.perogroupe.wecheapis.exceptions.NewRequestNotFoundException
import dev.perogroupe.wecheapis.exceptions.PendingRequestNotFoundException
import dev.perogroupe.wecheapis.exceptions.UserNotFoundException
import dev.perogroupe.wecheapis.repositories.CheckRequestStatusRepository
import dev.perogroupe.wecheapis.repositories.NewRequestRepository
import dev.perogroupe.wecheapis.repositories.PendingRequestRepository
import dev.perogroupe.wecheapis.repositories.RoleRepository
import dev.perogroupe.wecheapis.repositories.UserRepository
import dev.perogroupe.wecheapis.services.NotificationsService
import dev.perogroupe.wecheapis.services.PendingRequestService
import dev.perogroupe.wecheapis.services.UploadService
import dev.perogroupe.wecheapis.utils.enums.RequestStatus
import dev.perogroupe.wecheapis.utils.response
import dev.perogroupe.wecheapis.utils.toPendingRequest
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.time.Instant

@Service
class PendingRequestServiceImpl(
    private val repository: PendingRequestRepository,
    private val newRequestRepository: NewRequestRepository,
    private val eventPublisher: ApplicationEventPublisher,
    private val notificationsService: NotificationsService,
    private val userRepository: UserRepository,
    private val uploadService: UploadService,
    private val checkStatusRepository: CheckRequestStatusRepository,
    private val roleRepository: RoleRepository,
) : PendingRequestService {

    /**
     * Store a pending request and update the corresponding new request status.
     *
     * @param id The ID of the new request to be stored as pending.
     * @return The stored pending request response.
     * @throws NewRequestNotFoundException If the new request with the given ID is not found.
     * @throws UserNotFoundException If the user with the given serial number is not found.
     */
    override fun store(id: String): PendingRequestResponse =
        // Find the new request by ID
        newRequestRepository.findById(id)
            // If found, update the new request status to DONE and save it
            .map { newRequestRepository.save(it.copy(requestStatus = RequestStatus.DONE)) }
            // If the new request is updated successfully, proceed with storing the pending request
            .map {
                // Create a check request status request for the pending request
                val req = CheckRequestStatusReq(
                    requestNumber = it.requestNumber!!,
                    requestStatus = RequestStatus.PENDING,
                    user = it.user!!,
                    comment = "None",
                    createdBySupAt = Instant.now()
                )

                // Find the applicant's supervisor
                val userSupervisor = userRepository.findUserByStructureAndRole("ROLE_SUPER_ADMIN", it.user.structure!!)
                    // If the supervisor is not found, throw an exception
                    .orElseThrow { UserNotFoundException("User with this serial number ${it.user.structure!!} not found") }

                // Create a notification for the supervisor
                val notification = Notifications(
                    user = userSupervisor,
                    message = "Demande ${it.requestNumber} est en attente.",
                    read = false
                )
                // Store the notification
                notificationsService.store(notification)
                // Publish an event for the check request status
                eventPublisher.publishEvent(CheckRequestStatusEvent(this, req))
                it
            }
            // If the new request is not found, throw an exception
            .orElseThrow { NewRequestNotFoundException("New request with id ${id} not found") }
            // Convert the updated new request to a pending request and save it
            .toPendingRequest()
            .let { repository.save(it).response() }


    /**
     * Retrieves a list of pending requests sorted in descending order by creation date.
     *
     * @return List of PendingRequestResponse
     */
    override fun list(): List<PendingRequestResponse> =
        repository.findAll(Sort.by(Sort.Direction.DESC, "created_at")).map { it.response() }

    /**
     * Retrieves a list of pending requests by structure ID where the request status is not DONE,
     * sorted in ascending order by creation date.
     *
     * @param id the ID of the structure
     * @return List of PendingRequestResponse
     */
    override fun listByStructure(id: String): List<PendingRequestResponse> = repository
        .findAllByStructureIdAndRequestStatusIsNot(id, RequestStatus.DONE, Sort.by(Sort.Direction.DESC, "createdAt"))
        .map { it.response() }

    /**
     * Retrieves a page of pending requests sorted in ascending order by creation date.
     *
     * @param page the page number
     * @param size the size of the page
     * @return Page of PendingRequestResponse
     */
    override fun list(
        page: Int,
        size: Int,
    ): Page<PendingRequestResponse> {
        // Create a PageRequest object for pagination
        val pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "created_at"))

        // Retrieve all pending requests using the pageable object and map them to PendingRequestResponse
        return repository.findAll(pageable).map { it.response() }
    }

    /**
     * Updates a pending request with the given request ID.
     *
     * @param request the update request containing the request ID and reason for update
     * @return the updated pending request response
     * @throws NewRequestNotFoundException if the pending request with the given ID is not found
     */
    override fun update(
        request: UpdateRequestReq,
    ): PendingRequestResponse = repository
        // Find the pending request by ID
        .findById(request.requestId)
        // Copy the pending request and update its status to UPDATE
        .map { it.copy(requestStatus = RequestStatus.UPDATE) }
        // Create a new check request status request with the updated request number, user, and reason for update
        .map {
            val req = CheckRequestStatusReq(
                requestNumber = it.requestNumber!!,
                requestStatus = RequestStatus.UPDATE,
                user = it.user!!,
                comment = request.reasonOfUpdate
            )
            // Publish a check request status event with the updated request
            eventPublisher.publishEvent(CheckRequestStatusEvent(this, req))
            it
        }
        // Save the updated pending request and retrieve the updated response
        .map { repository.save(it) }
        .map { it.response() }
        // Throw an exception if the pending request with the given ID is not found
        .orElseThrow { NewRequestNotFoundException("Pending request with id ${request.requestId} not found") }

    /**
     * Retrieves a pending request response by its request ID.
     *
     * @param id The request ID to retrieve the pending request response.
     * @return The pending request response for the given request ID.
     * @throws NewRequestNotFoundException If no pending request is found with the given ID.
     */
    override fun show(id: String): PendingRequestResponse = repository
        .findByRequestNumber(id)
        .map { it.response() }
        .orElseThrow { NewRequestNotFoundException("Pending request with id $id not found") }



    /**
     * Counts the number of pending requests for a given user.
     *
     * @param authentication The authentication object containing the user's serial number.
     * @return The count of pending requests for the user.
     * @throws UserNotFoundException If the user with the given serial number is not found.
     */
    override fun count(authentication: Authentication): Long {
        // Find the user by their serial number
        val user = userRepository
            .findByUsername(authentication.name)
            .orElseThrow { UserNotFoundException("User with this serial number ${authentication.name} not found") }

        // Count the number of pending requests for the user's structure
        return repository.countByStructureAndRequestStatusIsNot(user.structure, RequestStatus.DONE)
    }

    override fun updateRequest(id: String): PendingRequest = repository.findByRequestNumber(id)
        .map { req ->
            req.requestStatus = RequestStatus.UPDATE
            repository.save(req)
        }.orElseThrow { PendingRequestNotFoundException("Pending request not found") }

    /**
     * Uploads appointment decree and handing over files for a pending request.
     *
     * @param appointmentDecree The appointment decree file to upload
     * @param handingOver The handing over file to upload
     * @param id The ID of the request
     * @param authentication The authentication details
     * @return PendingRequestResponse after uploading the files
     * @throws NewRequestNotFoundException If no pending request is found with the given ID.
     */
    override fun upload(
        appointmentDecree: MultipartFile,
        handingOver: MultipartFile,
        id: String,
        authentication: Authentication,
    ): PendingRequestResponse = repository.findById(id).map {
        if (handingOver == null) {
            // Copy the request with updated appointmentDecree and same handingOver
            val newRequest = it.copy(
                appointmentDecree = uploadService.uploadFile(
                    appointmentDecree,
                    "user_${appointmentDecree.originalFilename?.replace(" ".toRegex(), "_")?.lowercase()}_avatar.${
                        appointmentDecree.originalFilename?.substringAfterLast(".")
                    }",
                    "users/avatars/${appointmentDecree.originalFilename?.replace(" ".toRegex(), "_")?.lowercase()}/"
                ),
                handingOver = it.handingOver
            )
            // Update request status to PENDING
            val checkRequest = checkStatusRepository.findByRequestNumber(newRequest.requestNumber!!)
                .orElseThrow { PendingRequestNotFoundException("Request not found") }
            checkRequest.requestStatus = RequestStatus.PENDING
            checkStatusRepository.save(checkRequest)
            repository.save(newRequest)
            it.response()
        } else if (appointmentDecree == null) {
            // Copy the request with updated handingOver and same appointmentDecree
            val newRequest = it.copy(
                appointmentDecree = it.appointmentDecree,
                handingOver = uploadService.uploadFile(
                    handingOver,
                    "user_${handingOver.originalFilename?.replace(" ".toRegex(), "_")?.lowercase()}_avatar.${
                        handingOver.originalFilename?.substringAfterLast(".")
                    }",
                    "users/avatars/${handingOver.originalFilename?.replace(" ".toRegex(), "_")?.lowercase()}/"
                )
            )
            // Update request status to PENDING
            val checkRequest = checkStatusRepository.findByRequestNumber(newRequest.requestNumber!!)
                .orElseThrow { PendingRequestNotFoundException("Request not found") }
            checkRequest.requestStatus = RequestStatus.PENDING
            checkStatusRepository.save(checkRequest)
            repository.save(newRequest)
            it.response()
        } else {
            // Copy the request with updated appointmentDecree and handingOver
            val newRequest = it.copy(
                appointmentDecree = uploadService.uploadFile(
                    appointmentDecree,
                    "user_${appointmentDecree.originalFilename?.replace(" ".toRegex(), "_")?.lowercase()}_avatar.${
                        appointmentDecree.originalFilename?.substringAfterLast(".")
                    }",
                    "users/avatars/${appointmentDecree.originalFilename?.replace(" ".toRegex(), "_")?.lowercase()}/"
                ),
                handingOver = uploadService.uploadFile(
                    handingOver,
                    "user_${handingOver.originalFilename?.replace(" ".toRegex(), "_")?.lowercase()}_avatar.${
                        handingOver.originalFilename?.substringAfterLast(".")
                    }",
                    "users/avatars/${handingOver.originalFilename?.replace(" ".toRegex(), "_")?.lowercase()}/"
                )
            )
            // Update request status to PENDING
            val checkRequest = checkStatusRepository.findByRequestNumber(newRequest.requestNumber!!)
                .orElseThrow { PendingRequestNotFoundException("Request not found") }
            checkRequest.requestStatus = RequestStatus.PENDING
            checkStatusRepository.save(checkRequest)
            repository.save(newRequest)
            it.response()
        }
    }.orElseThrow { NewRequestNotFoundException("Pending request with id $id not found") }

}