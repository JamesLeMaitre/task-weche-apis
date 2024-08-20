package dev.perogroupe.wecheapis.controllers

import dev.perogroupe.wecheapis.dtos.requests.ContactRequest
import dev.perogroupe.wecheapis.dtos.responses.clients.HttpResponse
import dev.perogroupe.wecheapis.services.ContactService
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
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(API_BASE_URL + "contact", produces = [APPLICATION_JSON_VALUE])
class ContactController (
    private val service: ContactService
){
    @GetMapping("")
    fun list(): ResponseEntity<HttpResponse> = successResponse(
        "List of contact", OK, service.list()
    )

    @GetMapping("all")
    fun list(@RequestParam page: Int, @RequestParam size: Int): ResponseEntity<HttpResponse> = successResponse(
        "List of contact", OK, service.listAll(page, size)
    )

    @PostMapping("")
    fun store(@ModelAttribute @Valid request: ContactRequest, result: BindingResult): ResponseEntity<HttpResponse> =
        if (result.hasErrors()) {
            validationErrorResponse(result.fieldErrors)
        } else successResponse(
            "Contact submitted successfully!", OK, service.store(request)
        )

    @GetMapping("show/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    fun show(@PathVariable id: String): ResponseEntity<HttpResponse> = successResponse(
        "Structure found successfully!", OK, service.show(id)
    )
}