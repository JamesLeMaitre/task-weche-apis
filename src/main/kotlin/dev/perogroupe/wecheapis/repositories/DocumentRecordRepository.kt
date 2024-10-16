package dev.perogroupe.wecheapis.repositories

import dev.perogroupe.wecheapis.entities.DocumentRecord
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.Optional

interface DocumentRecordRepository : JpaRepository<DocumentRecord, String> {
    fun findByReference(reference: String): Optional<DocumentRecord>
    // Write functionto check if exist by request number

    fun findByRequestNumber(requestNumber: String): Optional<DocumentRecord>
}