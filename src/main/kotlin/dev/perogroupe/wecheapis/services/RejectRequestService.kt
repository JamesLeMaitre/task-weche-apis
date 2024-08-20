package dev.perogroupe.wecheapis.services

import dev.perogroupe.wecheapis.dtos.requests.RejectedRequestReq
import dev.perogroupe.wecheapis.dtos.responses.RejectedRequestResponse
import org.springframework.data.domain.Page
import org.springframework.security.core.Authentication

interface RejectRequestService {
    fun store(request: RejectedRequestReq): RejectedRequestResponse
    fun storeForSupAdmin(request: RejectedRequestReq): RejectedRequestResponse
    fun list(): List<RejectedRequestResponse>
    fun list(page: Int, size: Int): Page<RejectedRequestResponse>

    fun show (requestNumber: String): RejectedRequestResponse

    fun count(authentication: Authentication): Long
}