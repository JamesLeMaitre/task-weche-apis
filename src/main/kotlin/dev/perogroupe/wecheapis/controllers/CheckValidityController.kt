package dev.perogroupe.wecheapis.controllers

import dev.perogroupe.wecheapis.dtos.responses.clients.HttpResponse
import dev.perogroupe.wecheapis.services.CheckValidityService
import dev.perogroupe.wecheapis.utils.API_BASE_URL
import dev.perogroupe.wecheapis.utils.successResponse
import org.springframework.http.HttpStatus.OK
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(API_BASE_URL + "check-validity", produces = [APPLICATION_JSON_VALUE])
class CheckValidityController(
    private val service: CheckValidityService
) {
    @GetMapping("app")
    fun checkValidityApp(): ResponseEntity<HttpResponse> = successResponse(
        "Check if user has a valid app validity",
        OK,
        service.checkValidityApp(SecurityContextHolder.getContext().authentication)
    )

    @GetMapping("both")
    fun checkValidityUser(): ResponseEntity<HttpResponse> = successResponse(
        "Check if user has a valid app and dnr validity",
        OK,
        service.checkValidity(SecurityContextHolder.getContext().authentication)
    )

}