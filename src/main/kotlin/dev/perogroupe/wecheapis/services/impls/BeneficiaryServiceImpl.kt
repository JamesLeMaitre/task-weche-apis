package dev.perogroupe.wecheapis.services.impls

import dev.perogroupe.wecheapis.dtos.requests.BeneficiaryRequest
import dev.perogroupe.wecheapis.dtos.responses.BeneficiaryResponse
import dev.perogroupe.wecheapis.exceptions.BeneficiaryNotFoundException
import dev.perogroupe.wecheapis.repositories.BeneficiaryRepository
import dev.perogroupe.wecheapis.services.BeneficiaryService
import dev.perogroupe.wecheapis.utils.response
import dev.perogroupe.wecheapis.utils.toBeneficiary
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class BeneficiaryServiceImpl(
    private val repository: BeneficiaryRepository
): BeneficiaryService {
    override fun store(request: BeneficiaryRequest): BeneficiaryResponse {
        val beneficiary = request.toBeneficiary()
        return repository.save(beneficiary).response()
    }

    override fun findByName(name: String): BeneficiaryResponse {
        return repository.findByName(name).map {
            it.response()
        }.orElseThrow {
            BeneficiaryNotFoundException("Beneficiary not found")
        }
    }

    override fun findAll(): List<BeneficiaryResponse> =  repository.findAll(Sort.by(Sort.Direction.DESC, "createdAt")).map  { it.response()}
}