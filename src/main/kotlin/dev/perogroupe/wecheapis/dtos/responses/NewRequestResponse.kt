package dev.perogroupe.wecheapis.dtos.responses

import dev.perogroupe.wecheapis.utils.enums.RequestStatus
import lombok.Data
import lombok.ToString
import java.io.Serializable
import java.time.Instant
import java.util.Date

@Data
@ToString
data class NewRequestResponse(
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
    val requestNumber: String,
    val firstNameOfPreviousOfficial: String,
    val lastNameOfPreviousOfficial: String,
    val serialNumberOfPreviousOfficial: String,
    val gradeOfPreviousOfficial: String,
    val positionHeldOfPreviousOfficial: String,
    val bodyOfPreviousOfficial: String,
    val requestStatus: RequestStatus,
    val title: String,
//    val user: UserResponse? = null
): Serializable
