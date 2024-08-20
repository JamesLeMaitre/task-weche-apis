package dev.perogroupe.wecheapis.dtos.responses

import lombok.Data
import lombok.ToString
import java.io.Serializable
import java.time.Instant

@Data
@ToString
data class HelloRetirementResponse(
    val id: String ,
    val firstName: String,
    val lastName: String,
    val yearOfDeparture: String,
    val phoneNumber: String,
    val email: String,
    val birthDate: String,
    val emergencyContact: String,
    val subject: String,
    val serialNumber: String,
    val message: String,
    val structure: StructureResponse? = null,
    val createdAt: Instant
): Serializable
