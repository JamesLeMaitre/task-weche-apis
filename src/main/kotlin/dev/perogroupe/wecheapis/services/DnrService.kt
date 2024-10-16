package dev.perogroupe.wecheapis.services

import dev.perogroupe.wecheapis.dtos.requests.DnrRequest
import dev.perogroupe.wecheapis.dtos.responses.DnrResponse
import org.springframework.security.core.Authentication

interface DnrService {
    fun createDnr(request: DnrRequest): DnrResponse

    fun checkDnrValidity(authentication: Authentication): Boolean
}