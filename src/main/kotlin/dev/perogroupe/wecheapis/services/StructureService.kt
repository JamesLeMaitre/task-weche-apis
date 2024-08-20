package dev.perogroupe.wecheapis.services

import dev.perogroupe.wecheapis.dtos.requests.StructureRequest
import dev.perogroupe.wecheapis.dtos.responses.StructureResponse

interface StructureService {
    fun store(request: StructureRequest): StructureResponse
    fun update(id: String, request: StructureRequest) : StructureResponse
    fun delete(id: String) : String
    fun show(id: String) : StructureResponse
    fun list() : List<StructureResponse>
}