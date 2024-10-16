package dev.perogroupe.wecheapis.services.impls

import dev.perogroupe.wecheapis.dtos.requests.DocumentRecordRequest
import dev.perogroupe.wecheapis.entities.DocumentRecord
import dev.perogroupe.wecheapis.exceptions.DocumentRecordNotFoundException
import dev.perogroupe.wecheapis.repositories.DocumentRecordRepository
import dev.perogroupe.wecheapis.services.DocumentRecordService
import dev.perogroupe.wecheapis.utils.toDocumentRecord
import org.springframework.stereotype.Service

@Service
class DocumentRecordServiceImpl(
    private val repository: DocumentRecordRepository
) : DocumentRecordService {
    /**
     * Stores a document record in the database.
     *
     * @param request the document record request
     * @return the stored document record
     */
    override fun store(request: DocumentRecordRequest): DocumentRecord {
        val existBy = repository.findByRequestNumber(request.requestNumber)
        if (existBy.isPresent) {
            return existBy.get()
//            throw DocumentRecordNotFoundException("Document record already exists")
        }
        val documentRecord = request.toDocumentRecord()
        repository.save(documentRecord)
        return documentRecord

    }


    /**
     * Searches for a document record by its serial number.
     *
     * @param serialNumber the serial number of the document record to search for
     * @return the found document record
     */
    override fun search(serialNumber: String): ByteArray =
        repository.findByReference(serialNumber)
            .map { it.pdfData }
            .orElseThrow { DocumentRecordNotFoundException("Document record not found") }


}