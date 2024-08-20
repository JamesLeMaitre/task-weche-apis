package dev.perogroupe.wecheapis.services.impls

import dev.perogroupe.wecheapis.dtos.requests.StructureRequest
import dev.perogroupe.wecheapis.dtos.responses.StructureResponse
import dev.perogroupe.wecheapis.exceptions.StructureNotFoundException
import dev.perogroupe.wecheapis.repositories.StructureRepository
import dev.perogroupe.wecheapis.services.StructureService
import dev.perogroupe.wecheapis.utils.AppConverter
import dev.perogroupe.wecheapis.utils.response
import dev.perogroupe.wecheapis.utils.toStructure
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class StructureServiceImpl(
    private val repository: StructureRepository,
) : StructureService {
    override fun store(request: StructureRequest): StructureResponse {
        val structure = request.toStructure()
        return repository.save(structure).response()
    }

    override fun update(
        id: String,
        request: StructureRequest,
    ): StructureResponse = repository.findById(id).map {
        val structure = request.toStructure()
        repository.save(structure).response()
    }.orElseThrow {
        StructureNotFoundException("Structure not found")
    }

    override fun delete(id: String): String = repository.findById(id).map {
        repository.delete(it)
        "Structure deleted successfully"
    }.orElseThrow {
        StructureNotFoundException("Structure not found")
    }

    override fun show(id: String): StructureResponse = repository.findById(id).map {
        it.response()
    }.orElseThrow {
        StructureNotFoundException("Structure not found")
    }

    override fun list(): List<StructureResponse> = repository.findAll(Sort.by(Sort.Direction.ASC, "createdAt")).map {
        it.response()
    }

}