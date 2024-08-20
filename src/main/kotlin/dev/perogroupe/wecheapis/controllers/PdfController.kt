package dev.perogroupe.wecheapis.controllers

import dev.perogroupe.wecheapis.services.PdfService
import dev.perogroupe.wecheapis.utils.API_BASE_URL
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(API_BASE_URL + "file", produces = [APPLICATION_JSON_VALUE])
class PdfController(
    private val service: PdfService,
) {

    /**
     * Endpoint to generate a PDF for a given request number.
     *
     * @param requestNumber The number of the request for which to generate the PDF.
     * @return A ResponseEntity containing the generated PDF as a byte array.
     */
    @GetMapping("pdf/{requestNumber}")
    fun generatePdf(@PathVariable requestNumber: String): ResponseEntity<ByteArray> {
        // Generate the PDF using the provided request number
        val pdfBytes = service.generatePdf(requestNumber)

        // Return the generated PDF as a ResponseEntity
        return ResponseEntity.ok(pdfBytes)
    }
}