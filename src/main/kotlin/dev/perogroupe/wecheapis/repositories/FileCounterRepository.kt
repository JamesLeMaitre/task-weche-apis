package dev.perogroupe.wecheapis.repositories

import dev.perogroupe.wecheapis.entities.FileCounter
import dev.perogroupe.wecheapis.utils.enums.FileType
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface FileCounterRepository: JpaRepository<FileCounter, String> {
    fun countByFileType(fileType: FileType): Int

    fun findByReference(reference: String): Optional<FileCounter>
}