package dev.perogroupe.wecheapis.services

import dev.perogroupe.wecheapis.dtos.requests.HelloRetirementRequest
import dev.perogroupe.wecheapis.dtos.responses.HelloRetirementResponse

interface HelloRetirementService {
    fun create(request: HelloRetirementRequest): HelloRetirementResponse
    fun show(id: String): HelloRetirementResponse
    fun delete(id: String): String
    fun list(): List<HelloRetirementResponse>
    fun listByStructure(structureId: String): List<HelloRetirementResponse>
    fun listByStructureAndYear(structureId: String, year: String): List<HelloRetirementResponse>

}