package dev.perogroupe.wecheapis.dtos.responses

import dev.perogroupe.wecheapis.utils.enums.RequestStatus
import java.time.Instant
import java.util.Date

import lombok.Data
import lombok.ToString
import java.io.Serializable

@Data
@ToString
data class ApprovedRequestResponse(
    val id: String ,
    val firstName: String,
    val lastName: String,
    val structure: StructureResponse? = null,
    val civilName: String,
    val createdAt: Instant,
    val startPeriod: Date,
    val endPeriod: Date,
    val dateOfFirstEntryService: Date,
    val appointmentDecree: FileResponse? = null,
    val handingOver: FileResponse? = null,
    val requestNumber: String? = null,
    val firstNameOfPreviousOfficial: String,
    val lastNameOfPreviousOfficial: String,
    val serialNumberOfPreviousOfficial: String,
    val gradeOfPreviousOfficial: String,
    val positionHeldOfPreviousOfficial: String,
    val bodyOfPreviousOfficial: String,
    val requestStatus: RequestStatus,
    val user : UserResponse? = null
) : Serializable
