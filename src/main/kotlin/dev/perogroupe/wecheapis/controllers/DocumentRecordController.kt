package dev.perogroupe.wecheapis.controllers

import dev.perogroupe.wecheapis.services.DocumentRecordService
import dev.perogroupe.wecheapis.utils.API_BASE_URL
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(API_BASE_URL + "document-records", produces = [APPLICATION_JSON_VALUE])
class DocumentRecordController(
    private val service: DocumentRecordService
) {


    @GetMapping("search")
    fun searchDocument(@RequestParam("reference") reference: String): ResponseEntity<ByteArray> {
        // Generate the PDF using the provided request number
        val pdfBytes = service.search(reference)

        // Return the generated PDF as a ResponseEntity
        return ResponseEntity.ok(pdfBytes)
    }
}