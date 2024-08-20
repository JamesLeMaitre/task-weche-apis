package dev.perogroupe.wecheapis.dtos.requests

import dev.perogroupe.wecheapis.entities.User
import dev.perogroupe.wecheapis.utils.enums.RequestStatus
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import lombok.Data
import lombok.ToString
import java.io.Serializable
import java.time.Instant

@Data
@ToString
data class CheckRequestStatusReq(
    @field:NotBlank(message = "Request number is required")
    @field:NotNull(message = "Request number is required")
    val requestNumber: String,
    @field:NotBlank(message = "Request status is required")
    @field:NotNull(message = "Request status is required")
    val requestStatus: RequestStatus,
    @field:NotBlank(message = "User is required")
    @field:NotNull(message = "User is required")
    val user: User,
    @field:NotBlank(message = "Comment is required")
    @field:NotNull(message = "Comment is required")
    val comment: String,
    val createByDpafAt: Instant? = null,
    val createdBySupAt: Instant? = null,
):Serializable
