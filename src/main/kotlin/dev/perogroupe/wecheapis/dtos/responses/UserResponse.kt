package dev.perogroupe.wecheapis.dtos.responses

import dev.jtm.reportcardapisweb.dtos.responses.RoleResponse
import lombok.Data
import lombok.ToString
import java.io.Serializable
import java.util.*

@Data
@ToString
class UserResponse(
    val id: String,
    val username: String,
    val firstname: String,
    val lastname: String,
    val phoneNumber: String,
    val serialNumber: String,
    val profession: String,
    val birthdate: Date,
    val email: String,
    val roles: Collection<RoleResponse>,
    val avatar: FileResponse? = null,
    val structure: StructureResponse? = null,
    val requestNumber: String? = null,
    val hasRequested: Boolean = false,
    val body: String, // Corps
    val grade: String, // Grade
    val gradeDate: Date, // Grade date
    val ppsDate: Date,
    val oldUserFirstname: String,
    val oldUserLastname: String,
    val oldUserSerialNumber: String,
    val placeOfBirth: String,
    val ua: String? = null,
    val dateRetreat: Date? = null,
) : Serializable