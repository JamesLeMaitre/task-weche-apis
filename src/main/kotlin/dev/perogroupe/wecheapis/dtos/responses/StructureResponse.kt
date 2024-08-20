package dev.perogroupe.wecheapis.dtos.responses

import lombok.Data
import lombok.ToString
import java.time.Instant
import java.util.UUID

@Data
@ToString
data class StructureResponse(
    val id: String,
    val name: String,
    val createdAt: Instant
)
