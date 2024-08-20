package dev.perogroupe.wecheapis.controllers

import dev.perogroupe.wecheapis.dtos.requests.RejectedRequestReq
import dev.perogroupe.wecheapis.dtos.responses.clients.HttpResponse
import dev.perogroupe.wecheapis.services.NewRequestService
import dev.perogroupe.wecheapis.services.UploadService
import dev.perogroupe.wecheapis.utils.API_BASE_URL
import dev.perogroupe.wecheapis.utils.determineContentType
import dev.perogroupe.wecheapis.utils.successResponse
import dev.perogroupe.wecheapis.utils.validationErrorResponse
import jakarta.validation.Valid
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus.OK
import org.springframework.http.MediaType
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping(API_BASE_URL + "new-request", produces = [APPLICATION_JSON_VALUE])
class NewRequestController(
    private val service: NewRequestService,
    private val uploadService: UploadService,
) {

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_USER')")
    fun store(
        @RequestParam("firstName", required = true) firstName: String,
        @RequestParam("lastName", required = true) lastName: String,
        @RequestParam("structureId", required = true) structureId: String,
        @RequestParam("civilName", required = true) civilName: String,
        @RequestParam("startPeriod", required = true) startPeriod: String,
        @RequestParam("endPeriod", required = true) endPeriod: String,
        @RequestParam("dateOfFirstEntryService", required = true) dateOfFirstEntryService: String,
        @RequestParam("firstNameOfPreviousOfficial", required = true) firstNameOfPreviousOfficial: String,
        @RequestParam("lastNameOfPreviousOfficial", required = true) lastNameOfPreviousOfficial: String,
        @RequestParam("serialNumberOfPreviousOfficial", required = true) serialNumberOfPreviousOfficial: String,
        @RequestParam("gradeOfPreviousOfficial", required = true) gradeOfPreviousOfficial: String,
        @RequestParam("positionHeldOfPreviousOfficial", required = true) positionHeldOfPreviousOfficial: String,
        @RequestParam("bodyOfPreviousOfficial", required = true) bodyOfPreviousOfficial: String,
        @RequestParam("appointmentDecree", required = true   ) appointmentDecree: MultipartFile,
        @RequestParam("handingOver", required = true) handingOver: MultipartFile
    ): ResponseEntity<HttpResponse> =
        successResponse(
            "New request created successfully!", OK, service.store(
                firstName,
                lastName,
                structureId,
                civilName,
                startPeriod,
                endPeriod,
                dateOfFirstEntryService,
                firstNameOfPreviousOfficial,
                lastNameOfPreviousOfficial,
                serialNumberOfPreviousOfficial,
                gradeOfPreviousOfficial,
                positionHeldOfPreviousOfficial,
                bodyOfPreviousOfficial,
                appointmentDecree,
                handingOver,
                SecurityContextHolder.getContext().authentication
            )
        )


    @GetMapping("user/{fileName:.*}")
    fun download(@PathVariable fileName: String): ResponseEntity<Resource> {
        val resource = uploadService.loadFile(fileName)
        val mediaType: MediaType = determineContentType(fileName)
        val headers = HttpHeaders()
        headers.contentType = mediaType

        return ResponseEntity
            .ok()
            .headers(headers)
            .body(resource)
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_ADMIN')")
    fun show(@PathVariable id: String): ResponseEntity<HttpResponse> = successResponse(
        "Structure created successfully!", OK, service.show(id)
    )


    @GetMapping("list-by-structure/{id}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_ADMIN')")
    fun listByStructure(@PathVariable id: String): ResponseEntity<HttpResponse> = successResponse(
        "List of new requests by structure successfully  returned!", OK, service.listByStructure(id)
    )

    @GetMapping("list-all-structure/{id}")
    fun listPageByStructure(@PathVariable id: String,@RequestParam("page") page: Int,@RequestParam("size") size: Int): ResponseEntity<HttpResponse> = successResponse(
        "List of new requests by structure successfully  returned!", OK, service.listByStructure(id,page,size)
    )

    @GetMapping("count")
    fun count(): ResponseEntity<HttpResponse> = successResponse(
        "Count requests by structure successfully", OK, service.count(SecurityContextHolder.getContext().authentication)
    )
}