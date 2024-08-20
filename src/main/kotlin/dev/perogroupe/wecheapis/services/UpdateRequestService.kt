package dev.perogroupe.wecheapis.services

import dev.perogroupe.wecheapis.dtos.requests.UpRequest
import dev.perogroupe.wecheapis.dtos.responses.UpdateRequestResponse

interface UpdateRequestService {
    fun store(request: UpRequest): UpdateRequestResponse
    fun show(id:String): UpdateRequestResponse
}