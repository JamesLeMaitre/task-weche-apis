package dev.perogroupe.wecheapis.dtos.requests

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import lombok.Data
import lombok.ToString
import java.io.Serializable

@Data
@ToString
data class RoleRequest(
    @field:NotNull(message = "Role name is required")
    @field:NotBlank(message = "Role name is required")
    @field:NotBlank
    val roleName: String
):Serializable
