package dev.perogroupe.wecheapis.dtos.requests

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import lombok.Data
import lombok.ToString
import java.io.Serializable

@Data
@ToString
data class ContactRequest(
    @field:NotNull(message = "Name is required")
    @field:NotBlank(message = "Name is required")
    val name: String? = null,
    @field:NotNull(message = "Subject is required")
    @field:NotBlank(message = "Subject is required")
    val subject: String? = null,
    @field:NotNull(message = "Email is required")
    @field:NotBlank(message = "Email is required")
    @field:Email(message = "Email is not valid", regexp = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-z]{2,7}$")
    val email: String? = null,
    @field:NotNull(message = "Message is required")
    @field:NotBlank(message = "Message is required")
    val message: String? = null,
    @field:NotNull(message = "Serial number is required")
    @field:NotBlank(message = "Serial number is required")
    val serialNumber: String? = null,
    @field:NotNull(message = "Phone number is required")
    @field:NotBlank(message = "Phone number is required")
    val phoneNumber: String? = null,
):Serializable
