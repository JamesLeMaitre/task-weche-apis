package dev.perogroupe.wecheapis.controllers

import dev.perogroupe.wecheapis.dtos.requests.StructureRequest
import dev.perogroupe.wecheapis.dtos.responses.clients.HttpResponse
import dev.perogroupe.wecheapis.services.StructureService
import dev.perogroupe.wecheapis.utils.API_BASE_URL
import dev.perogroupe.wecheapis.utils.successResponse
import dev.perogroupe.wecheapis.utils.validationErrorResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus.OK
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(API_BASE_URL + "structure", produces = [APPLICATION_JSON_VALUE])
class StructureController(
    private val service: StructureService,
) {
    @GetMapping("")
    fun list(): ResponseEntity<HttpResponse> = successResponse(
        "List of structures", OK, service.list()
    )

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_S_SUPER_ADMIN')")
    fun store(@ModelAttribute @Valid request: StructureRequest, result: BindingResult): ResponseEntity<HttpResponse> =
        if (result.hasErrors()) {
            validationErrorResponse(result.fieldErrors)
        } else successResponse(
            "Structure created successfully!", CREATED, service.store(request)
        )

    @PostMapping("update/{id}")
    @PreAuthorize("hasRole('ROLE_S_SUPER_ADMIN')")
    fun update(
        @PathVariable id: String,
        @ModelAttribute @Valid request: StructureRequest,
        result: BindingResult
    ): ResponseEntity<HttpResponse> = if (result.hasErrors()) {
        validationErrorResponse(result.fieldErrors)
    } else successResponse(
        "Structure updated successfully!", OK, service.update(id, request)
    )

    @GetMapping("show/{id}")
    @PreAuthorize("hasRole('ROLE_S_SUPER_ADMIN')")
    fun show(@PathVariable id: String): ResponseEntity<HttpResponse> = successResponse(
        "Structure found successfully!", OK, service.show(id)
    )

    @DeleteMapping("delete/{id}")
    @PreAuthorize("hasRole('ROLE_S_SUPER_ADMIN')")
    fun delete(@PathVariable id: String): ResponseEntity<HttpResponse> = successResponse(
        "Structure deleted successfully!", OK, service.delete(id)
    )

}