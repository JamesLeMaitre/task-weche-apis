package dev.perogroupe.wecheapis.dtos.requests

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import lombok.Data
import lombok.ToString

@Data
@ToString
data class StructureRequest(
    @field:NotNull(message = "Structure name is required")
    @field:NotBlank(message = "Structure name is required")
    val name: String? = null
)
