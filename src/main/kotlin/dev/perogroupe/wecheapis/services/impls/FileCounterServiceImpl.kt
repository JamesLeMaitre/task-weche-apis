package dev.perogroupe.wecheapis.services.impls

import dev.perogroupe.wecheapis.entities.FileCounter
import dev.perogroupe.wecheapis.repositories.FileCounterRepository
import dev.perogroupe.wecheapis.services.FileCounterService
import dev.perogroupe.wecheapis.utils.enums.FileType
import org.springframework.stereotype.Service

@Service
class FileCounterServiceImpl(
    private val fileCounterRepository: FileCounterRepository
) : FileCounterService {
    override fun increment(request: FileType, reference: String): Int {
        val existBy = fileCounterRepository.findByReference(reference)
        if (existBy.isPresent) {
            return existBy.get().count
        } else {
            val count = fileCounterRepository.countByFileType(request)
            fileCounterRepository.save(FileCounter(count = count + 1, reference = reference, fileType = request))
            return count + 1
        }
    }

    override fun decrement(reference: String) {
        TODO("Not yet implemented")
    }

    override fun get(reference: String): Int {
        TODO("Not yet implemented")
    }
}