package dev.perogroupe.wecheapis.dtos.requests

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import lombok.Data
import lombok.ToString
import java.io.Serializable

@Data
@ToString
data class NewRequestReq(
    // Old fields
//    val startPeriod: String? = null,
//    val endPeriod: String? = null,
//    val dateOfFirstEntryService: String? = null,

//    val gradeOfPreviousOfficial: String? = null,
//    val positionHeldOfPreviousOfficial: String? = null,
//    val bodyOfPreviousOfficial: String? = null,

    // TODO: New request fields
    // Common fields


    // For first select beneficiary
    val firstName: String? = null,
    val lastName: String? = null,
    val structureId: String? = null,
    val beneficiaryId: String? = null,
    val serialNumber: String? = null,
    val body: String? = null, // Corps
    val grade: String? = null, // Grade
    val gradeDate: String? = null, // Grade date
    val ua: String? = null, // UA
    val positionHeld: String? = null, // Poste de l'agent
    val ppsDate: String? = null, // Date de PPS
    val uaDate: String? = null,
//    val agentPosition: String? = null,

    // For second select beneficiary
    val firstNameOfPreviousOfficial: String? = null,
    val lastNameOfPreviousOfficial: String? = null,
    val fonction: String? = null,
    val dateFonction: String? = null,
    val serialNumberOfPreviousOfficial: String? = null,


    ) : Serializable
