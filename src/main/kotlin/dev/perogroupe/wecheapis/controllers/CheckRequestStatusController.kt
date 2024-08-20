package dev.perogroupe.wecheapis.controllers

import dev.perogroupe.wecheapis.dtos.responses.clients.HttpResponse
import dev.perogroupe.wecheapis.services.CheckRequestStatusService
import dev.perogroupe.wecheapis.utils.API_BASE_URL
import dev.perogroupe.wecheapis.utils.successResponse
import org.springframework.http.HttpStatus.OK
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(API_BASE_URL + "check-request-status", produces = [APPLICATION_JSON_VALUE])
class CheckRequestStatusController(
    private val service: CheckRequestStatusService,
) {
    /**
     * Endpoint to search for a check request status by request number.
     *
     * @param requestNumber The request number to search for.
     * @return A ResponseEntity containing the HttpResponse with the status of the check request.
     */
    @GetMapping("search/{requestNumber}")
    fun search(@PathVariable requestNumber: String): ResponseEntity<HttpResponse> {
        // Call the service to search for the check request status by request number
        val checkRequestStatus = service.searchCheckRequestStatusByRequestNumber(requestNumber)

        // Return a success response with the status of the check request
        return successResponse(
            "Status of check request",
            OK,
            checkRequestStatus
        )
    }

    /**
     * Endpoint to list the check request status based on the provided structureId.
     *
     * @param structureId the id of the structure for which to retrieve the check request list
     * @return the ResponseEntity containing the HttpResponse with the status of the check request list
     */
    @GetMapping("list/{structureId}")
    fun list(@PathVariable structureId: String): ResponseEntity<HttpResponse> {
        // Call the service to list the check request status based on the structureId
        val checkRequestList = service.listRequest(structureId)

        // Return a success response with the status of the check request list
        return successResponse("Status of check request list", OK, checkRequestList)
    }


    /**
     * Retrieves the status of the check request list for Admin based on the provided structureId.
     *
     * @param structureId the id of the structure for which to retrieve the check request list
     * @return the ResponseEntity containing the HttpResponse with the status of the check request list for Admin
     */
    @GetMapping("list-all-admin/{structureId}")
    fun listAllAdmin(@PathVariable structureId: String): ResponseEntity<HttpResponse> = successResponse(
        "Status of check request list for Admin", OK, service.listRequestForAdmin(structureId)
    )


    /**
     * Endpoint to list all check request statuses for DPAF based on the structureId.
     *
     * @param structureId The id of the structure for which to retrieve the check request list.
     * @return A ResponseEntity containing the HttpResponse with the status of the check request list.
     */
    @GetMapping("list-all-dpaf/{structureId}")
    fun listAllDpaf(@PathVariable structureId: String): ResponseEntity<HttpResponse> {
        // Call the service to list all check request statuses for DPAF based on the structureId
        val checkRequestStatusList = service.listRequestForDpaf(structureId)

        // Return a success response with the status of the check request list
        return successResponse(
            "Status of check request list for DPAF",
            OK,
            checkRequestStatusList
        )
    }

    /**
     * Endpoint to list all check requests that have been approved or rejected.
     *
     * @return A ResponseEntity containing the HttpResponse with the status of the check request list.
     */
    @GetMapping("list-approved-rejected")
    fun listApprovedRejected(): ResponseEntity<HttpResponse> {
        // Get the current authentication from the security context
        val authentication = SecurityContextHolder.getContext().authentication

        // Call the service to list all check requests that have been approved or rejected
        val checkRequestStatusList = service.listRequestApprovedOrRejected(authentication)

        // Return a success response with the status of the check request list
        return successResponse(
            "Status of check request list approved rejected",
            OK,
            checkRequestStatusList
        )
    }

    /**
     * Endpoint to search for check requests based on the provided search string.
     *
     * @param search The search string to search for.
     * @return A ResponseEntity containing the HttpResponse with the status of the check request list.
     */

    @GetMapping("search-from-user/{search}")
    fun searchFromUser(@PathVariable search: String): ResponseEntity<HttpResponse> {
        val checkRequestStatusList = service.listRequestFromSearch(search)
        return successResponse("Status of check request list", OK, checkRequestStatusList)
    }

}