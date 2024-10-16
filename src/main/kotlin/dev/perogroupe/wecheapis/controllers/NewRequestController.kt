package dev.perogroupe.wecheapis.controllers

import dev.perogroupe.wecheapis.dtos.requests.RejectedRequestReq
import dev.perogroupe.wecheapis.dtos.responses.clients.HttpResponse
import dev.perogroupe.wecheapis.events.DnrEvent
import dev.perogroupe.wecheapis.services.NewRequestService
import dev.perogroupe.wecheapis.services.UploadService
import dev.perogroupe.wecheapis.utils.API_BASE_URL
import dev.perogroupe.wecheapis.utils.determineContentType
import dev.perogroupe.wecheapis.utils.successResponse
import dev.perogroupe.wecheapis.utils.validationErrorResponse
import jakarta.validation.Valid
import org.springframework.context.ApplicationEventPublisher
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
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
    private val eventPublisher: ApplicationEventPublisher
) {

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_USER')")
    fun store(
        @RequestParam("firstName", required = true) firstName: String,
        @RequestParam("lastName", required = true) lastName: String,
        @RequestParam("structureId", required = true) structureId: String,
        @RequestParam("beneficiaryId", required = true) beneficiaryId: String,
//        @RequestParam("startPeriod", required = false) startPeriod: String,
//        @RequestParam("endPeriod", required = false) endPeriod: String,
//        @RequestParam("dateOfFirstEntryService", required = false) dateOfFirstEntryService: String?,
        @RequestParam("firstNameOfPreviousOfficial", required = false) firstNameOfPreviousOfficial: String?,
        @RequestParam("lastNameOfPreviousOfficial", required = false) lastNameOfPreviousOfficial: String?,
        @RequestParam("serialNumberOfPreviousOfficial", required = false) serialNumberOfPreviousOfficial: String?,
//        @RequestParam("gradeOfPreviousOfficial", required = false) gradeOfPreviousOfficial: String?,
//        @RequestParam("positionHeldOfPreviousOfficial", required = false) positionHeldOfPreviousOfficial: String?,
//        @RequestParam("bodyOfPreviousOfficial", required = false) bodyOfPreviousOfficial: String?,
        @RequestParam("appointmentDecree", required = false) appointmentDecree: MultipartFile? = null,
        @RequestParam("handingOver", required = false,) handingOver: MultipartFile? = null,

        @RequestParam("serialNumber", required = false) serialNumber: String?,
        @RequestParam("body", required = false) body: String?,
        @RequestParam("grade", required = false) grade: String?,
        @RequestParam("gradeDate", required = false) gradeDate: String?,
        @RequestParam("ua", required = false) ua: String?,
        @RequestParam("positionHeld", required = false) positionHeld: String?,
//        @RequestParam("agentPosition", required = false) agentPosition: String?,
        @RequestParam("ppsDate", required = false) ppsDate: String?,
        @RequestParam("uaDate", required = false) uaDate: String?,
        @RequestParam("fonction", required = false) fonction: String?,
        @RequestParam("dateFonction", required = false) dateFonction: String?,

    ): ResponseEntity<HttpResponse> {
        eventPublisher.publishEvent(DnrEvent(this))
        // Handle optional file for appointmentDecree
        if (appointmentDecree != null && !appointmentDecree.isEmpty) {
            // Process the appointmentDecree file
            println("Processing appointmentDecree")
        } else {
            println("No appointmentDecree provided")
        }

        // Handle optional file for handingOver
        if (handingOver != null && !handingOver.isEmpty) {
            // Process the handingOver file
            println("Processing handingOver")
        } else {
            println("No handingOver provided")
        }

        // Call your service with the optional parameters, including the file fields
        val newRequest = service.store(
            firstName,
            lastName,
            structureId,
            beneficiaryId,
//            dateOfFirstEntryService!!,
            firstNameOfPreviousOfficial!!,
            lastNameOfPreviousOfficial!!,
            serialNumberOfPreviousOfficial!!,
//            gradeOfPreviousOfficial!!,
//            positionHeldOfPreviousOfficial!!,
//            bodyOfPreviousOfficial!!,
            appointmentDecree!!,  // These files can be null if not provided
            handingOver!!,        // These files can be null if not provided
            SecurityContextHolder.getContext().authentication,
            serialNumber!!,
            body!!,
            grade!!,
            gradeDate!!,
            ua!!,
            positionHeld!!,
//            agentPosition!!,
            ppsDate!!,
            uaDate!!,
            fonction!!,
            dateFonction!!
        )

        return successResponse("New request created successfully!", HttpStatus.OK, newRequest)
    }

    @PostMapping("create")
    @PreAuthorize("hasRole('ROLE_USER')")
    fun create(
        @RequestParam("firstName", required = true) firstName: String,
        @RequestParam("lastName", required = true) lastName: String,
        @RequestParam("structureId", required = true) structureId: String,
        @RequestParam("beneficiaryId", required = true) beneficiaryId: String,
//        @RequestParam("startPeriod", required = false) startPeriod: String,
//        @RequestParam("endPeriod", required = false) endPeriod: String,
//        @RequestParam("dateOfFirstEntryService", required = false) dateOfFirstEntryService: String?,
        @RequestParam("firstNameOfPreviousOfficial", required = false) firstNameOfPreviousOfficial: String?,
        @RequestParam("lastNameOfPreviousOfficial", required = false) lastNameOfPreviousOfficial: String?,
        @RequestParam("serialNumberOfPreviousOfficial", required = false) serialNumberOfPreviousOfficial: String?,
//        @RequestParam("gradeOfPreviousOfficial", required = false) gradeOfPreviousOfficial: String?,
//        @RequestParam("positionHeldOfPreviousOfficial", required = false) positionHeldOfPreviousOfficial: String?,
//        @RequestParam("bodyOfPreviousOfficial", required = false) bodyOfPreviousOfficial: String?,
//        @RequestParam("appointmentDecree", required = false) appointmentDecree: MultipartFile? = null,
//        @RequestParam("handingOver", required = false,) handingOver: MultipartFile? = null,

        @RequestParam("serialNumber", required = false) serialNumber: String?,
        @RequestParam("body", required = false) body: String?,
        @RequestParam("grade", required = false) grade: String?,
        @RequestParam("gradeDate", required = false) gradeDate: String?,
        @RequestParam("ua", required = false) ua: String?,
        @RequestParam("positionHeld", required = false) positionHeld: String?,
//        @RequestParam("agentPosition", required = false) agentPosition: String?,
        @RequestParam("ppsDate", required = false) ppsDate: String?,
        @RequestParam("uaDate", required = false) uaDate: String?,
        @RequestParam("fonction", required = false) fonction: String?,
        @RequestParam("dateFonction", required = false) dateFonction: String?,

        ): ResponseEntity<HttpResponse> {
        // Publish event
        eventPublisher.publishEvent(DnrEvent(this))


        // Call your service with the optional parameters, including the file fields
        val newRequest = service.create(
            firstName,
            lastName,
            structureId,
            beneficiaryId,
//            dateOfFirstEntryService!!,
            firstNameOfPreviousOfficial!!,
            lastNameOfPreviousOfficial!!,
            serialNumberOfPreviousOfficial!!,
//            gradeOfPreviousOfficial!!,
//            positionHeldOfPreviousOfficial!!,
//            bodyOfPreviousOfficial!!,
//            appointmentDecree!!,  // These files can be null if not provided
//            handingOver!!,        // These files can be null if not provided
            SecurityContextHolder.getContext().authentication,
            serialNumber!!,
            body!!,
            grade!!,
            gradeDate!!,
            ua!!,
            positionHeld!!,
//            agentPosition!!,
            ppsDate!!,
            uaDate!!,
            fonction!!,
            dateFonction!!
        )

        return successResponse("New request created successfully!", HttpStatus.OK, newRequest)
    }


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

    @GetMapping("show/{requestNumber}")
    fun showByRequestNumber(@PathVariable requestNumber: String): ResponseEntity<HttpResponse> = successResponse(
        "Show request by request number successfully", OK, service.showByRequestNumber(requestNumber)
    )
}