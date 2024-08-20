package dev.perogroupe.wecheapis.dtos.responses

import dev.perogroupe.wecheapis.entities.PendingRequest
import lombok.Data
import lombok.ToString

@Data
@ToString
data class UpdateRequestResponse(
    val id: String,
    val appointmentDecree: Boolean,
    val handingOver: Boolean,
    val reason: String,
    var request: PendingRequestResponse? = null
)
