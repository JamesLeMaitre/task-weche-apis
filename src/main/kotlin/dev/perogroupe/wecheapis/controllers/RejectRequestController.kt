package dev.perogroupe.wecheapis.controllers

import dev.perogroupe.wecheapis.dtos.requests.RejectedRequestReq
import dev.perogroupe.wecheapis.dtos.responses.clients.HttpResponse
import dev.perogroupe.wecheapis.services.RejectRequestService
import dev.perogroupe.wecheapis.utils.API_BASE_URL
import dev.perogroupe.wecheapis.utils.successResponse
import dev.perogroupe.wecheapis.utils.validationErrorResponse
import jakarta.validation.Valid
import jakarta.websocket.server.PathParam
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping

@RestController
@RequestMapping(API_BASE_URL + "reject-request", produces = [APPLICATION_JSON_VALUE])
class RejectRequestController (
    private val service: RejectRequestService
){

    @GetMapping("")
    fun list(): ResponseEntity<HttpResponse> = successResponse(
        "List of reject requests", OK, service.list()
    )

    @GetMapping("count")
    fun count(): ResponseEntity<HttpResponse> = successResponse(
        "Total of reject requests", OK, service.count(SecurityContextHolder.getContext().authentication)
    )

    @GetMapping("show/{requestNumber}")
    fun show(@PathVariable requestNumber: String): ResponseEntity<HttpResponse> = successResponse(
        "Show reject  requests", OK, service.show(requestNumber)
    )
    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    fun storeAdmin(@ModelAttribute @Valid request: RejectedRequestReq, result: BindingResult): ResponseEntity<HttpResponse> =
        if (result.hasErrors()) {
            validationErrorResponse(result.fieldErrors)
        } else successResponse(
            "Pending request created successfully!", OK, service.store(request)
        )


    @PostMapping("store-sup-admin")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    fun storeSupAdmin(@ModelAttribute @Valid request: RejectedRequestReq, result: BindingResult): ResponseEntity<HttpResponse> =
        if (result.hasErrors()) {
            validationErrorResponse(result.fieldErrors)
        } else successResponse(
            "Pending request created successfully!", OK, service.storeForSupAdmin(request)
        )
}