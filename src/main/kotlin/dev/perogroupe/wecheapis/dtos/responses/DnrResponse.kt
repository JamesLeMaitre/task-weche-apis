package dev.perogroupe.wecheapis.dtos.responses

import lombok.Data
import java.time.Instant
import java.util.Date

@Data
data class DnrResponse(
    val id: String,
    val user: UserResponse?,
    val validityStart: Date,
    val validityEnd: Date,
    val newRequest: NewRequestResponse?,
    val valid: Boolean,
    val createdAt: Instant
)
