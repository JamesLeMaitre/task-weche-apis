package dev.perogroupe.wecheapis.dtos.responses

import lombok.Data
import lombok.ToString

@Data
@ToString
data class ContactResponse(
    val id: String,
    val name: String,
    val email: String,
    val subject: String,
    val message: String,
    val serialNumber: String,
    val phoneNumber: String
)
