package dev.perogroupe.wecheapis.services

import dev.perogroupe.wecheapis.dtos.requests.BeneficiaryRequest
import dev.perogroupe.wecheapis.dtos.responses.BeneficiaryResponse

interface BeneficiaryService {
    fun store(request: BeneficiaryRequest): BeneficiaryResponse

    fun findByName(name: String): BeneficiaryResponse

    fun findAll(): List<BeneficiaryResponse>
}