package dev.perogroupe.wecheapis.dtos.responses

import lombok.Data
import lombok.ToString
import java.io.Serializable
import java.util.UUID

@Data
@ToString
data class FileResponse(
    val id: String,
    val name: String,
    val url: String,
    val type: String
): Serializable
