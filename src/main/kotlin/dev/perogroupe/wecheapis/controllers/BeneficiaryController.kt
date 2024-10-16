package dev.perogroupe.wecheapis.controllers

import dev.perogroupe.wecheapis.dtos.requests.BeneficiaryRequest
import dev.perogroupe.wecheapis.dtos.responses.clients.HttpResponse
import dev.perogroupe.wecheapis.services.BeneficiaryService
import dev.perogroupe.wecheapis.utils.API_BASE_URL
import dev.perogroupe.wecheapis.utils.successResponse
import dev.perogroupe.wecheapis.utils.validationErrorResponse
import jakarta.validation.Valid
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(API_BASE_URL + "beneficiaries", produces = [APPLICATION_JSON_VALUE])
class BeneficiaryController(
    private val beneficiaryService: BeneficiaryService,
) {
    @GetMapping("")
    fun list(): ResponseEntity<HttpResponse> = successResponse(
        "List of beneficiaries", OK, beneficiaryService.findAll()
    )

    /**
     * Endpoint to store beneficiaries.
     *
     * @param request The beneficiary request object containing the beneficiary data.
     * @param result The binding result object containing any validation errors.
     * @return A response entity containing the result of the store operation.
     */
    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_S_SUPER_ADMIN')")
    fun store(@ModelAttribute @Valid request: BeneficiaryRequest, result: BindingResult): ResponseEntity<HttpResponse> =
        if (result.hasErrors()) {
            validationErrorResponse(result.fieldErrors)
        } else successResponse("Beneficiary submitted successfully!", OK, beneficiaryService.store(request))

    /**
     * Endpoint to update beneficiaries.
     * This endpoint expects a PUT request with a JSON body containing the beneficiary data.
     */
    @GetMapping("show-by-name/{name}")
    fun show(@PathVariable name: String): ResponseEntity<HttpResponse> = successResponse(
        "Beneficiary found successfully!", OK, beneficiaryService.findByName(name)
    )
}