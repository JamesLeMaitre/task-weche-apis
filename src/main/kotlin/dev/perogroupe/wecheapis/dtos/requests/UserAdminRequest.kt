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
data class UserAdminRequest(
    @field:NotNull @field:NotBlank
    val firstname: String? = null,
    @field:NotNull @field:NotBlank
    val lastname: String? = null,
    @field:Email @field:NotNull @field:NotBlank
    val email: String? = null,
    @field:NotNull @field:NotBlank @field:Size(min = 6, max = 12)
    val password: String? = null,
    @field:NotNull @field:NotBlank @field:Size(min = 6, max = 12)
    val confirmPassword: String? = null,
    @field:NotNull @field:NotBlank
    val phoneNumber: String? = null,
    @field:NotNull @field:NotBlank
    val serialNumber: String? = null,
    @field:NotNull @field:NotBlank @field:BirthDate
    val birthdate: String? = null,
    @field:NotNull @field:NotBlank
    val structureId: String? = null,
    @field:NotNull @field:NotBlank
    val roleName: String? = null,
):Serializable
