package dev.perogroupe.wecheapis.services

import dev.perogroupe.wecheapis.dtos.requests.CheckRequestStatusReq
import dev.perogroupe.wecheapis.dtos.responses.CheckRequestStatusResponse
import org.springframework.security.core.Authentication

interface CheckRequestStatusService {
    fun store(request: CheckRequestStatusReq): String
    fun searchCheckRequestStatusByRequestNumber(requestNumber: String): CheckRequestStatusResponse
    fun listRequest(structureId: String): List<CheckRequestStatusResponse>
    fun listRequestForAdmin(structureId: String): List<CheckRequestStatusResponse>
    fun listRequestForDpaf(structureId: String): List<CheckRequestStatusResponse>
    fun listRequestApprovedOrRejected(authentication: Authentication): List<CheckRequestStatusResponse>

    fun listRequestFromSearch(search: String): List<CheckRequestStatusResponse>
}