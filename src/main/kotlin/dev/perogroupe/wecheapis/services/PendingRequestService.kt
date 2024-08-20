package dev.perogroupe.wecheapis.services

import dev.perogroupe.wecheapis.dtos.requests.UpdateRequestReq
import dev.perogroupe.wecheapis.dtos.responses.PendingRequestResponse
import dev.perogroupe.wecheapis.entities.PendingRequest
import org.springframework.data.domain.Page
import org.springframework.security.core.Authentication
import org.springframework.web.multipart.MultipartFile

interface PendingRequestService {
    fun store(id: String): PendingRequestResponse

    fun list(): List<PendingRequestResponse>
    fun listByStructure(id: String): List<PendingRequestResponse>

    fun list(page: Int, size: Int): Page<PendingRequestResponse>

    fun update(request: UpdateRequestReq): PendingRequestResponse

    fun show(id: String): PendingRequestResponse

    fun count(authentication: Authentication): Long

    fun updateRequest(id:String): PendingRequest

    fun upload( appointmentDecree: MultipartFile,
                handingOver: MultipartFile,id: String,authentication: Authentication): PendingRequestResponse
}