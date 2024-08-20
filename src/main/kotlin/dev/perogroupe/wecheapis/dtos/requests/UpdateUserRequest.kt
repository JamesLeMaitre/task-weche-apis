package dev.perogroupe.wecheapis.dtos.requests

import dev.perogroupe.wecheapis.utils.validations.validators.BirthDate
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import lombok.Data
import lombok.ToString
import java.io.Serializable

@Data
@ToString
class UpdateUserRequest(
    @field:NotNull @field:NotBlank
    val firstname: String? = null,
    @field:NotNull @field:NotBlank
    val profession: String? = null,
    @field:NotNull @field:NotBlank
    val lastname: String? = null,
    @field:NotNull @field:NotBlank
    val phoneNumber: String? = null,
    @field:NotNull @field:NotBlank
    val serialNumber: String? = null,
    @field:NotNull @field:NotBlank @field:BirthDate
    val birthdate: String? = null,
):Serializable