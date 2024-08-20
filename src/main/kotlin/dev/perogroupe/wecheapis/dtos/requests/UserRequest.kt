package dev.perogroupe.wecheapis.dtos.requests

import dev.perogroupe.wecheapis.utils.validations.validators.BirthDate
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import lombok.Data
import lombok.ToString
import java.io.Serializable

@Data
@ToString
data class UserRequest(
    @field:NotNull(message = "Firstname is required")
    @field:NotBlank(message = "Firstname is required")
    val firstname: String? = null,
    @field:NotNull(message = "Lastname is required")
    @field:NotBlank(message = "Lastname is required")
    val lastname: String? = null,
    @field:Email(message = "Invalid email address", regexp = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")
    @field:NotNull(message = "Email is required")
    @field:NotBlank(message = "Email is required")
    val email: String? = null,
    @field:NotNull(message = "Password is required")
    @field:NotBlank(message = "Password is required")
    @field:Size(min = 6, max = 12, message = "Password must be between 6 and 12 characters")
    val password: String? = null,
    @field:NotNull(message = "Confirm password is required")
    @field:NotBlank(message = "Confirm password is required")
    @field:Size(min = 6, max = 12, message = "Confirm password must be between 6 and 12 characters")
    val confirmPassword: String? = null,
    @field:NotNull(message = "Phone number is required")
    @field:NotBlank(message = "Phone number is required")
    val phoneNumber: String? = null,
    @field:NotNull(message = "Serial number is required")
    @field:NotBlank(message = "Serial number is required")
    val serialNumber: String? = null,
    @field:NotNull(message = "Birthdate is required")
    @field:NotBlank(message = "Birthdate is required")
    @field:BirthDate(message = "Birthdate is required")
    val birthdate: String? = null,
) : Serializable
