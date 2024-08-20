package dev.perogroupe.wecheapis.controllers

import dev.perogroupe.wecheapis.dtos.responses.clients.HttpResponse
import dev.perogroupe.wecheapis.services.NotificationsService
import dev.perogroupe.wecheapis.utils.API_BASE_URL
import dev.perogroupe.wecheapis.utils.successResponse
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.http.HttpStatus.OK
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(API_BASE_URL + "notifications", produces = [APPLICATION_JSON_VALUE])
class NotificationsController (
    private val service: NotificationsService
){



    @GetMapping("unread")
    fun listNotReadByUser(): ResponseEntity<HttpResponse> = successResponse(
        "List of notifications not read", OK, service.listNotReadByUser(SecurityContextHolder.getContext().authentication)
    )

    @GetMapping("read/{id}")
    fun readNotifications(@PathVariable id: String): ResponseEntity<HttpResponse> = successResponse(
        "Read notifications !", OK, service.read(id)
    )

}