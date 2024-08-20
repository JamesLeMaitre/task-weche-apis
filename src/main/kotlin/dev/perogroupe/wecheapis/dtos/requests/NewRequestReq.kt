package dev.perogroupe.wecheapis.dtos.requests

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import lombok.Data
import lombok.ToString
import java.io.Serializable

@Data
@ToString
data class NewRequestReq(
    @field:NotNull(message = "First name is required") @field:NotBlank(message = "First name is required")
    val firstName: String? = null,
    @field:NotNull(message = "Last name is required") @field:NotBlank(message = "Last name is required")
    val lastName: String? = null,
    @field:NotNull(message = "Structure id is required") @field:NotBlank(message = "Structure id is required")
    val structureId: String? = null,
    @field:NotNull(message = "Civil name is required") @field:NotBlank(message = "Civil name is required")
    val civilName: String? = null,
    @field:NotNull(message = "Start period is required") @field:NotBlank(message = "Start period is required")
    val startPeriod: String? = null,
    @field:NotNull(message = "End period is required") @field:NotBlank(message = "End period is required")
    val endPeriod: String? = null,
    @field:NotNull(message = "Date of first entry service is required") @field:NotBlank(message = "Date of first entry service is required")
    val dateOfFirstEntryService: String? = null,
    @field:NotNull(message = "First name of previous official is required") @field:NotBlank(message = "First name of previous official is required")
    val firstNameOfPreviousOfficial: String? = null,
    @field:NotNull(message = "Last name of previous official is required") @field:NotBlank(message = "Last name of previous official is required")
    val lastNameOfPreviousOfficial: String? = null,
    @field:NotNull(message = "Serial number of previous official is required") @field:NotBlank(message = "Serial number of previous official is required")
    val serialNumberOfPreviousOfficial: String? = null,
    @field:NotNull(message = "Grade of previous official is required") @field:NotBlank(message = "Grade of previous official is required")
    val gradeOfPreviousOfficial: String? = null,
    @field:NotNull(message = "Position held of previous official is required") @field:NotBlank(message = "Position held of previous official is required")
    val positionHeldOfPreviousOfficial: String? = null,
    @field:NotNull(message = "Body of previous official is required") @field:NotBlank(message = "Body of previous official is required")
    val bodyOfPreviousOfficial: String? = null,
) : Serializable
