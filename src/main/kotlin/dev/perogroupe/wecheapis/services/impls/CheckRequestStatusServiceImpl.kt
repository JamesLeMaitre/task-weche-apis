package dev.perogroupe.wecheapis.services.impls

import dev.perogroupe.wecheapis.dtos.requests.CheckRequestStatusReq
import dev.perogroupe.wecheapis.dtos.responses.CheckRequestStatusResponse
import dev.perogroupe.wecheapis.exceptions.CheckRequestStatusException
import dev.perogroupe.wecheapis.repositories.CheckRequestStatusRepository
import dev.perogroupe.wecheapis.repositories.UserRepository
import dev.perogroupe.wecheapis.services.CheckRequestStatusService
import dev.perogroupe.wecheapis.utils.enums.RequestStatus
import dev.perogroupe.wecheapis.utils.response
import dev.perogroupe.wecheapis.utils.toCheckRequestStatus
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class CheckRequestStatusServiceImpl(
    private val userRepository: UserRepository,
    private val repository: CheckRequestStatusRepository,
) : CheckRequestStatusService {

    /**
     * Store a new or update an existing check request status.
     *
     * @param request The check request status to store.
     * @return A message indicating the success of the operation.
     * @throws CheckRequestStatusException If the request number already exists and the request status is not approved or update.
     */
    override fun store(request: CheckRequestStatusReq): String {
        // Check if the request number already exists
        if (repository.existsCheckRequestStatusByRequestNumber(request.requestNumber)) {
            // Find the existing check status by request number
            val checkStatus = repository
                .findByRequestNumber(request.requestNumber)
                .orElseThrow { CheckRequestStatusException("Request with request number ${request.requestNumber} not initialized") }

            // Update the check status based on the request status
            if (request.requestStatus === RequestStatus.APPROVED || request.requestStatus === RequestStatus.UPDATE) {
                // Update the check status with the new request status, comment, and current timestamp
                repository.save(
                    checkStatus.copy(
                        requestStatus = request.requestStatus,
                        comment = request.comment,
                        createByDpafAt = Instant.now(),
                        createdBySupAt = checkStatus.createdBySupAt
                    )
                )
            } else {
                // Update the check status with the new request status, comment, current timestamp, and the original createByDpafAt value
                repository.save(
                    checkStatus.copy(
                        requestStatus = request.requestStatus,
                        comment = request.comment,
                        createdBySupAt = Instant.now(),
                        createByDpafAt = request.createByDpafAt
                    )
                )
            }

            return "Request successfully updated"
        } else {
            // Save the new check status
            repository.save(request.toCheckRequestStatus())
            return "Request saved successfully"
        }
    }

    /**
     * Searches for a check request status by request number.
     *
     * @param requestNumber the request number to search for
     * @return the response containing the check request status
     * @throws CheckRequestStatusException if the request number is not found
     */
    override fun searchCheckRequestStatusByRequestNumber(requestNumber: String): CheckRequestStatusResponse {
        // Search for the check request status by request number
        return repository
            .searchCheckRequestStatusByRequestNumber(requestNumber)
            .orElseThrow {
                // Throw an exception if the request number is not found
                CheckRequestStatusException("Request with request number $requestNumber not initialized")
            }
            .response()
    }

    override fun listRequest(structureId: String): List<CheckRequestStatusResponse> = repository
        .findAllByStructureIdAndRequestStatusIsNot(structureId, RequestStatus.NEW)
        .map { it.response() }


    /**
     * Lists all check request statuses for admin based on the structureId.
     *
     * @param structureId the id of the structure for which to list the check request statuses
     * @return the list of check request statuses for admin
     */
    override fun listRequestForAdmin(structureId: String): List<CheckRequestStatusResponse> {
        // Retrieve all check request statuses for admin ordered by creation date
        return repository
            .findAllByStructureIdOrderByCreatedAtDesc(structureId)
            .map { it.response() }
    }


    /**
     * Lists all check request statuses for DPAF based on the structureId.
     *
     * @param structureId the id of the structure for which to list the check request statuses
     * @return the list of check request statuses for DPAF
     */
    override fun listRequestForDpaf(structureId: String): List<CheckRequestStatusResponse> {
        // Retrieve all check request statuses for DPAF that are not new and have a DPAF creation timestamp
        return repository
            .findAllByRequestStatusIsNotAndAndCreateByDpafAtIsNotNull(structureId, Instant.now())
            .map { it.response() }
    }


    /**
     * Lists all check request statuses that are approved or rejected for a specific user.
     *
     * @param authentication the authentication object containing the user's information
     * @return the list of check request statuses that are approved or rejected
     */
    override fun listRequestApprovedOrRejected(authentication: Authentication): List<CheckRequestStatusResponse> {
        // Find the user by username, throw an exception if not found
        val user = userRepository.findByUsername(authentication.name).orElseThrow {
            CheckRequestStatusException("User with this serial number ${authentication.name} not found")
        }
        // Retrieve all check request statuses for the user and filter by approved or rejected statuses
        return repository.findAllByUserOrderByCreatedAtDesc(user)
            .filter { it.requestStatus === RequestStatus.APPROVED || it.requestStatus === RequestStatus.REJECTED }
            .map { it.response() }
    }

    override fun listRequestFromSearch(search: String): List<CheckRequestStatusResponse> =
        repository.findAllByUser_FirstnameOrUser_Profession(search, search).map { it.response() }


}