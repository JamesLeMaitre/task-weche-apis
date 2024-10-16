package dev.perogroupe.wecheapis.dtos.requests

import dev.perogroupe.wecheapis.entities.User
import lombok.Data
import java.io.Serializable
import java.util.Date

@Data
data class DnrRequest(
    val user: User,
    val validityStart: Date,
    val valid: Boolean,
    val requestNumber: String,
) : Serializable
