package dev.perogroupe.wecheapis.controllers

import dev.perogroupe.wecheapis.dtos.requests.HelloRetirementRequest
import dev.perogroupe.wecheapis.dtos.responses.clients.HttpResponse
import dev.perogroupe.wecheapis.services.HelloRetirementService
import dev.perogroupe.wecheapis.utils.API_BASE_URL
import org.springframework.http.HttpStatus.OK
import dev.perogroupe.wecheapis.utils.successResponse
import dev.perogroupe.wecheapis.utils.validationErrorResponse
import jakarta.validation.Valid
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
@RequestMapping(API_BASE_URL + "hello-retirement", produces = [APPLICATION_JSON_VALUE])
class HelloRetirementController (
    private val service: HelloRetirementService
){

    @GetMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    fun list(): ResponseEntity<HttpResponse> = successResponse(
        "List of hello retirements", OK, service.list()
    )

    @GetMapping("show/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    fun show(@PathVariable id: String): ResponseEntity<HttpResponse> = successResponse(
        "Hello Retirement found successfully!", OK, service.show(id)
    )

    @GetMapping("delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    fun delete(@PathVariable id: String): ResponseEntity<HttpResponse> = successResponse(
        "Hello Retirement deleted successfully!", OK, service.delete(id)
    )

    @GetMapping("list-by-structure/{structureId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    fun listByStructure(@PathVariable structureId: String): ResponseEntity<HttpResponse> = successResponse(
        "List of hello retirements", OK, service.listByStructure(structureId)
    )

    @GetMapping("list-by-structure/{structureId}/year/{year}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    fun listByStructureAndYear(@PathVariable structureId: String, @PathVariable year: String): ResponseEntity<HttpResponse> = successResponse(
        "List of hello retirements", OK, service.listByStructureAndYear(structureId, year)
    )

    @PostMapping("")
    fun store(@ModelAttribute @Valid request: HelloRetirementRequest, result: BindingResult): ResponseEntity<HttpResponse> =
        if (result.hasErrors()) {
            validationErrorResponse(result.fieldErrors)
        } else successResponse(
            "Hello Retirement created successfully!", OK, service.create(request)
        )


}