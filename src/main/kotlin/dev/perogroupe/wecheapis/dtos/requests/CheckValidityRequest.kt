package dev.perogroupe.wecheapis.dtos.requests

import dev.perogroupe.wecheapis.entities.User
import lombok.Data
import lombok.ToString
import java.io.Serializable
import java.time.Instant

@Data
@ToString
data class CheckValidityRequest(
    val user: User,
    val appDocument: Boolean,
    val appDnr: Boolean,
    val appDocumentDateDelivery: Instant,
    val appDnrDateDelivery: Instant,
) : Serializable
