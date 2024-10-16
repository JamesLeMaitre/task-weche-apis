package dev.perogroupe.wecheapis.controllers

import dev.perogroupe.wecheapis.dtos.requests.UpRequest
import dev.perogroupe.wecheapis.dtos.responses.clients.HttpResponse
import dev.perogroupe.wecheapis.services.UpdateRequestService
import dev.perogroupe.wecheapis.utils.API_BASE_URL
import dev.perogroupe.wecheapis.utils.successResponse
import dev.perogroupe.wecheapis.utils.validationErrorResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.OK
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = [API_BASE_URL + "update-request"], produces = [APPLICATION_JSON_VALUE])
class UpdateRequestController(
    private val service: UpdateRequestService,
) {

    /**
     * Updates a file based on the provided UpRequest.
     *
     * @param request The UpRequest containing the update information.
     * @param result The binding result for validation errors.
     * @return A response entity with the result of the update operation.
     */
    @PostMapping("")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    fun updateFile(@ModelAttribute @Valid request: UpRequest, result: BindingResult): ResponseEntity<HttpResponse> {
        // Check for validation errors
        return if (result.hasErrors()) {
            validationErrorResponse(result.fieldErrors)
        } else {
            // If there are no errors, update the file and return a success response
            successResponse("Request set for update successfully!", CREATED, service.store(request))
        }
    }


    @PostMapping("store")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    fun updateFileByAdmin(@ModelAttribute @Valid request: UpRequest, result: BindingResult): ResponseEntity<HttpResponse> {
        // Check for validation errors
        return if (result.hasErrors()) {
            validationErrorResponse(result.fieldErrors)
        } else {
            // If there are no errors, update the file and return a success response
            successResponse("Request set for update successfully!", CREATED, service.create(request))
        }
    }

    /**
     * Retrieves and shows an update response based on the provided request number.
     *
     * @param requestNumber The number associated with the update request.
     * @return A response entity containing the update response.
     */
    @GetMapping("show/{requestNumber}")
    fun show(@PathVariable requestNumber: String): ResponseEntity<HttpResponse> =
        successResponse("Show update response", OK, service.show(requestNumber))
}