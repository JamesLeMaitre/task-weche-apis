package dev.perogroupe.wecheapis.controllers

import dev.perogroupe.wecheapis.dtos.responses.clients.HttpResponse
import dev.perogroupe.wecheapis.services.DnrService
import dev.perogroupe.wecheapis.utils.API_BASE_URL
import dev.perogroupe.wecheapis.utils.successResponse
import org.springframework.http.HttpStatus.OK
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(API_BASE_URL + "dnrs", produces = [APPLICATION_JSON_VALUE])
class DnrController(
    private val service: DnrService,
) {
    @GetMapping("check")
    @PreAuthorize("hasRole('ROLE_USER')")
    fun list(): ResponseEntity<HttpResponse> = successResponse(
        "Check if dnr is valid", OK, service.checkDnrValidity(SecurityContextHolder.getContext().authentication)
    )

}