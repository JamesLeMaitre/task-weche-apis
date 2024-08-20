package dev.perogroupe.wecheapis.controllers

import dev.perogroupe.wecheapis.dtos.responses.clients.HttpResponse
import dev.perogroupe.wecheapis.services.ApprovedRequestService
import dev.perogroupe.wecheapis.utils.API_BASE_URL
import dev.perogroupe.wecheapis.utils.successResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus.OK
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(API_BASE_URL + "approved-request", produces = [APPLICATION_JSON_VALUE])
@Tag(name = "Weche APIS", description = "Weche API for demonstrating Swagger in Kotlin")
class ApprovedController(
    private val service: ApprovedRequestService,
) {

    /**
     * Endpoint to store approved requests by super admins.
     *
     * @param id The ID of the request to store
     * @return ResponseEntity containing the HttpResponse
     */
    @GetMapping("{id}")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    @Operation(summary = "Approve a request", description = "Approve a request")
    fun store(@PathVariable id: String): ResponseEntity<HttpResponse> = successResponse(
        "Approved request created successfully!", OK, service.store(id)
    )

    /**
     * Endpoint to count the total number of reject requests.
     *
     * @return ResponseEntity containing the HttpResponse with the count
     */
    @GetMapping("count")
    fun count(): ResponseEntity<HttpResponse> {
        /**
         * Retrieve the count of reject requests.
         */
        val count = service.count(SecurityContextHolder.getContext().authentication)

        /**
         * Return the successResponse with the count.
         */
        return successResponse("Total of reject requests", OK, count)
    }

    /*    @GetMapping("/download/{id}/pdf")
        fun getPdf(@PathVariable id: String): ResponseEntity<ByteArray> {
            val demande = // Fetch demande by id
            val pdfBytes = pdfService.generatePdf(demande)

            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"demande_$id.pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes)
        }*/
}