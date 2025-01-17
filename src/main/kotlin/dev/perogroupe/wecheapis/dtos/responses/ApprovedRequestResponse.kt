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
//    val firstName: String,
//    val lastName: String,
    val structure: StructureResponse? = null,
    val createdAt: Instant,
//    val startPeriod: Date,
//    val endPeriod: Date,
//    val dateOfFirstEntryService: Date,
    val appointmentDecree: FileResponse? = null,
    val handingOver: FileResponse? = null,
    val requestNumber: String? = null,
//    val firstNameOfPreviousOfficial: String,
//    val lastNameOfPreviousOfficial: String,
//    val serialNumberOfPreviousOfficial: String,
//    val gradeOfPreviousOfficial: String,
//    val positionHeldOfPreviousOfficial: String,
//    val bodyOfPreviousOfficial: String,
    val requestStatus: RequestStatus,
    val user : UserResponse? = null,

    val firstName: String,
    val lastName: String,
    val structureId: String? = null,
    val serialNumber: String? = null,
    val body: String? = null, // Corps
    val grade: String? = null, // Grade
    val gradeDate: Date? = null, // Grade date
    val ua: String? = null, // UA
    val positionHeld: String? = null, // Poste de l'agent
//

    // For second select beneficiary
    val firstNameOfPreviousOfficial: String? = null,
    val lastNameOfPreviousOfficial: String? = null,
    val fonction: String? = null,
    val dateFonction: Date? = null,
    val serialNumberOfPreviousOfficial: String? = null,
    // Step 2 attributes
//    val agentPosition: String? = null,
    val ppsDate: Date? = null,
    val uaDate: Date? = null, // Date d'entrée àl'UA
    val beneficiary: BeneficiaryResponse? = null,
) : Serializable
