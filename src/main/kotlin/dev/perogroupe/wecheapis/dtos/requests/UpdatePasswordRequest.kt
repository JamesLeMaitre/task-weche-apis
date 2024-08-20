package dev.perogroupe.wecheapis.dtos.requests

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import lombok.Data
import lombok.ToString
import java.io.Serializable

@Data
@ToString
data class UpdatePasswordRequest(
    @field:NotNull @field:NotBlank @field:Size(min = 8)
    val oldPassword: String? = null,
    @field:NotNull @field:NotBlank @field:Size(min = 8)
    val newPassword: String? = null,
    @field:NotNull @field:NotBlank @field:Size(min = 8)
    val confirmationPassword: String? = null
):Serializable
