package dev.perogroupe.wecheapis.controllers

import dev.perogroupe.wecheapis.dtos.requests.UpdateRequestReq
import dev.perogroupe.wecheapis.dtos.responses.clients.HttpResponse
import dev.perogroupe.wecheapis.services.PendingRequestService
import dev.perogroupe.wecheapis.utils.API_BASE_URL
import dev.perogroupe.wecheapis.utils.successResponse
import dev.perogroupe.wecheapis.utils.validationErrorResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.OK
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
@RequestMapping(API_BASE_URL + "pending-request", produces = [APPLICATION_JSON_VALUE])
class PendingRequestController(
    private val service: PendingRequestService,
) {

    @GetMapping("")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    fun list(): ResponseEntity<HttpResponse> = successResponse(
        "List of pending requests", OK, service.list()
    )


    @GetMapping("list-by-structure/{structureId}")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    fun listByStructure(@PathVariable structureId: String): ResponseEntity<HttpResponse> = successResponse(
        "List of pending requests by structure", OK, service.listByStructure(structureId)
    )

    @GetMapping("all")
    fun all(@RequestParam page: Int, @RequestParam size: Int): ResponseEntity<HttpResponse> = successResponse(
        "List of pending requests", OK, service.list(page, size)
    )

    @GetMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    fun store(@PathVariable id: String): ResponseEntity<HttpResponse> = successResponse(
        "Pending request created successfully!", CREATED, service.store(id)
    )

    @GetMapping("show/{id}")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    fun show(@PathVariable id: String): ResponseEntity<HttpResponse> = successResponse(
        "Pending request details successfully!", OK, service.show(id)
    )


    @PostMapping("update-file")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    fun updateFile(
        @ModelAttribute @Valid request: UpdateRequestReq,
        result: BindingResult,
    ): ResponseEntity<HttpResponse> =
        if (result.hasErrors()) {
            validationErrorResponse(result.fieldErrors)
        } else successResponse(
            "Pending request set for update successfully!", OK, service.update(request)
        )

/*    @PostMapping("update-file/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    fun updateFileUseRequest(
        @RequestParam("appointmentDecree", required = false, defaultValue = "") appointmentDecree: MultipartFile,
        @RequestParam("handingOver", required = false) handingOver: MultipartFile, @PathVariable id: String,
    ): ResponseEntity<HttpResponse> =
        successResponse(
            "Pending request set for update successfully!", OK, service.upload(
                appointmentDecree, handingOver, id,
                SecurityContextHolder.getContext().authentication
            )
        )*/


    @PostMapping("update-file/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    fun updateFileUseRequest(
        @RequestParam("appointmentDecree", required = false, defaultValue = "") appointmentDecree: MultipartFile,
        @RequestParam("handingOver", required = false) handingOver: MultipartFile, @PathVariable id: String,
    ): ResponseEntity<HttpResponse> =
        successResponse(
            "Pending request set for update successfully!", OK, service.uploadNew(
                appointmentDecree, handingOver, id,
                SecurityContextHolder.getContext().authentication
            )
        )

    @PostMapping("update-file-handing-over/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    fun updateFileHandingOverUseRequest(
        @RequestParam("handingOver", required = false) handingOver: MultipartFile, @PathVariable id: String,
    ): ResponseEntity<HttpResponse> =
        successResponse(
            "Pending request set for update handing over successfully!", OK, service.uploadHandingOver(
                handingOver, id,
                SecurityContextHolder.getContext().authentication
            )
        )

    @PostMapping("update-file-appointment-decree/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    fun updateFileAppointmentDecreeUseRequest(
        @RequestParam("appointmentDecree", required = false) handingOver: MultipartFile, @PathVariable id: String,
    ): ResponseEntity<HttpResponse> =
        successResponse(
            "Pending request set for update appointment decree successfully!", OK, service.uploadAppointmentDecree(
                handingOver, id,
                SecurityContextHolder.getContext().authentication
            )
        )

    @GetMapping("count")
    fun count(): ResponseEntity<HttpResponse> = successResponse(
        "Total of pending requests", OK, service.count(SecurityContextHolder.getContext().authentication)
    )

}