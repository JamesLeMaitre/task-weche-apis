package dev.perogroupe.wecheapis.dtos.requests

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import lombok.Data
import lombok.ToString
import java.io.Serializable

@Data
@ToString
data class UpRequest(
    val appointmentDecree: Boolean,
    val handingOver: Boolean,
    @field:NotNull(message = "Reason is required")
    @field:NotBlank(message = "Reason is required")
    val reason: String,
    @field:NotNull(message = "Request id is required")
    @field:NotBlank(message = "Request id is required")
    val requestId : String
) : Serializable
