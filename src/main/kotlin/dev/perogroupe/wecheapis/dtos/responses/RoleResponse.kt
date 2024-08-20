package dev.jtm.reportcardapisweb.dtos.responses

import lombok.Data
import lombok.ToString
import java.io.Serializable
import java.util.UUID

@Data
@ToString
data class RoleResponse(
    val id: String,
    val roleName: String
): Serializable
