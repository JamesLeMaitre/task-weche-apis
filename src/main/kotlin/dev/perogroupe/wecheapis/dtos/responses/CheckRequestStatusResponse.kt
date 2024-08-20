package dev.perogroupe.wecheapis.dtos.responses

import dev.perogroupe.wecheapis.utils.enums.RequestStatus
import lombok.Data
import lombok.ToString
import java.time.Instant

@Data
@ToString
data class CheckRequestStatusResponse(
    val id: String,
    val requestNumber: String,
    val requestStatus: RequestStatus,
    val user: UserResponse? = null,
    val structure: StructureResponse? = null,
    val comment: String? = null,
    val createdAt: Instant? = null,
    val createdBySupAt: Instant? = null,
    val createByDpafAt: Instant? = null,
)
