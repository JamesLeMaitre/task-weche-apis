package dev.perogroupe.wecheapis.services.impls

import dev.perogroupe.wecheapis.dtos.requests.HelloRetirementRequest
import dev.perogroupe.wecheapis.dtos.responses.HelloRetirementResponse
import dev.perogroupe.wecheapis.exceptions.HelloRetirementNotFoundException
import dev.perogroupe.wecheapis.exceptions.StructureNotFoundException
import dev.perogroupe.wecheapis.repositories.HelloRetirementRepository
import dev.perogroupe.wecheapis.repositories.StructureRepository
import dev.perogroupe.wecheapis.services.HelloRetirementService
import dev.perogroupe.wecheapis.utils.response
import dev.perogroupe.wecheapis.utils.toHelloRetirement
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class HelloRetirementServiceImpl(
    private val repository: HelloRetirementRepository,
    private val structureRepository: StructureRepository
) : HelloRetirementService {
    override fun create(request: HelloRetirementRequest): HelloRetirementResponse {
        val helloRetirement = request.toHelloRetirement().copy(
            structure = structureRepository.findById(request.structureId!!)
                .orElseThrow { StructureNotFoundException("Structure not found") }
        )
        return repository.save(helloRetirement).response()
    }

    override fun show(id: String): HelloRetirementResponse = repository.findById(id).map {
        it.response()
    }.orElseThrow {
        HelloRetirementNotFoundException("Hello Retirement not found")
    }

    override fun delete(id: String): String = repository.findById(id).map {
        repository.delete(it)
        "Hello Retirement deleted successfully"
    }.orElseThrow {
        HelloRetirementNotFoundException("Hello Retirement not found")
    }

    override fun list(): List<HelloRetirementResponse> = repository.findAll(Sort.by(Sort.Direction.ASC, "createdAt")).map {
        it.response()
    }

    override fun listByStructure(structureId: String): List<HelloRetirementResponse> = repository.findByStructureId(structureId).map {
        it.response()
    }

    override fun listByStructureAndYear(
        structureId: String,
        year: String,
    ): List<HelloRetirementResponse> = repository.findByStructureIdAndYearOfDeparture(structureId, year).map {
        it.response()
    }
}