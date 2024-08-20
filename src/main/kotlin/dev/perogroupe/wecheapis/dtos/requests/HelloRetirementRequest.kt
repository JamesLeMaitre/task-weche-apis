package dev.perogroupe.wecheapis.dtos.requests

import dev.perogroupe.wecheapis.utils.validations.validators.BirthDate
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import lombok.Data
import lombok.ToString
import java.io.Serializable

@Data
@ToString
data class HelloRetirementRequest(
    @field:NotNull(message = "First name is required")
    @field:NotBlank(message = "First name is required")
    val firstName: String? = null,
    @field:NotNull(message = "Last name is required")
    @field:NotBlank(message = "Last name is required")
    val lastName: String? = null,
    @field:NotNull(message = "Year of departure is required")
    @field:NotBlank(message = "Year of departure is required")
    val yearOfDeparture: String? = null,
    @field:NotNull(message = "Phone number is required")
    @field:NotBlank(message = "Phone number is required")
    val phoneNumber: String? = null,
    @field:NotNull(message = "Serial number is required")
    @field:NotBlank(message = "Serial number is required")
    val serialNumber: String? = null,
    @field:NotNull(message = "Email is required")
    @field:NotBlank(message = "Email is required")
    @field:Email(message = "Email is not valid", regexp = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-z]{2,7}$")
    val email: String? = null,
    @field:NotNull(message = "Birth date is required")
    @field:NotBlank(message = "Birth date is required")
    @field:BirthDate
    val birthDate: String? = null,
    @field:NotNull
    @field:NotBlank
    val emergencyContact: String? = null,
    @field:NotNull(message = "Subject is required")
    @field:NotBlank(message = "Subject is required")
    val subject: String? = null,
    @field:NotNull(message = "Message is required")
    @field:NotBlank(message = "Message is required")
    val message: String? = null,
    @field:NotNull(message = "Structure  is required")
    @field:NotBlank(message = "Structure  is required")
    val structureId: String? = null,
) : Serializable
