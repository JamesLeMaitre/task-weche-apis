package dev.perogroupe.wecheapis.services

import dev.perogroupe.wecheapis.dtos.requests.DocumentRecordRequest
import dev.perogroupe.wecheapis.entities.DocumentRecord

interface DocumentRecordService {
    fun store(request: DocumentRecordRequest): DocumentRecord

    fun search(serialNumber: String): ByteArray
}