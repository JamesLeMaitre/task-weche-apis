package dev.perogroupe.wecheapis.services

import dev.perogroupe.wecheapis.dtos.requests.NewRequestReq
import dev.perogroupe.wecheapis.dtos.requests.RejectedRequestReq
import dev.perogroupe.wecheapis.dtos.responses.NewRequestResponse
import dev.perogroupe.wecheapis.entities.NewRequest
import dev.perogroupe.wecheapis.entities.User
import org.springframework.data.domain.Page
import org.springframework.security.core.Authentication
import org.springframework.web.multipart.MultipartFile

interface NewRequestService {

    fun create(request: NewRequestReq): NewRequestResponse

    fun store(
        firstName: String,
        lastName: String,
        structureId: String,
        beneficiaryId: String,
//        startPeriod: String,
//        endPeriod: String,
//        dateOfFirstEntryService: String?,
        firstNameOfPreviousOfficial: String?,
        lastNameOfPreviousOfficial: String?,
        serialNumberOfPreviousOfficial: String?,
//        gradeOfPreviousOfficial: String?,
//        positionHeldOfPreviousOfficial: String?,
//        bodyOfPreviousOfficial: String?,
        appointmentDecree: MultipartFile?,
        handingOver: MultipartFile?,
        authentication: Authentication,

        serialNumber: String?,
        body: String?,
        grade: String?,
        gradeDate: String?,
        ua: String?,
        positionHeld: String?,
//        agentPosition: String?,
        ppsDate: String?,
        uaDate: String?,
        fonction: String?,
        dateFonction: String?,
    ): NewRequestResponse

    fun create(
        firstName: String,
        lastName: String,
        structureId: String,
        beneficiaryId: String,
//        startPeriod: String,
//        endPeriod: String,
//        dateOfFirstEntryService: String?,
        firstNameOfPreviousOfficial: String?,
        lastNameOfPreviousOfficial: String?,
        serialNumberOfPreviousOfficial: String?,
//        gradeOfPreviousOfficial: String?,
//        positionHeldOfPreviousOfficial: String?,
//        bodyOfPreviousOfficial: String?,
//        appointmentDecree: MultipartFile?,
//        handingOver: MultipartFile?,
        authentication: Authentication,

        serialNumber: String?,
        body: String?,
        grade: String?,
        gradeDate: String?,
        ua: String?,
        positionHeld: String?,
//        agentPosition: String?,
        ppsDate: String?,
        uaDate: String?,
        fonction: String?,
        dateFonction: String?,
    ): NewRequestResponse

    fun upload(appointmentDecree: MultipartFile, handingOver: MultipartFile, id: String,authentication: Authentication): NewRequestResponse

    fun show(id: String): NewRequestResponse


    fun listByStructure(structureId: String): List<NewRequestResponse>
    fun listByStructure(structureId: String, page: Int, size: Int): Page<NewRequestResponse>

    fun count(authentication: Authentication): Long

    fun showByRequestNumber(requestNumber: String): NewRequest
    fun showByRequest(requestNumber: String): NewRequest

}