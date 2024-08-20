package dev.perogroupe.wecheapis.utils

import dev.jtm.reportcardapisweb.dtos.responses.RoleResponse
import dev.perogroupe.wecheapis.dtos.requests.CheckRequestStatusReq
import dev.perogroupe.wecheapis.dtos.requests.ContactRequest
import dev.perogroupe.wecheapis.dtos.requests.HelloRetirementRequest
import dev.perogroupe.wecheapis.dtos.requests.NewRequestReq
import dev.perogroupe.wecheapis.dtos.requests.RoleRequest
import dev.perogroupe.wecheapis.dtos.requests.StructureRequest
import dev.perogroupe.wecheapis.dtos.requests.UpRequest
import dev.perogroupe.wecheapis.dtos.requests.UpdateUserRequest
import dev.perogroupe.wecheapis.dtos.requests.UserAdminRequest
import dev.perogroupe.wecheapis.dtos.requests.UserRequest
import dev.perogroupe.wecheapis.dtos.responses.ApprovedRequestResponse
import dev.perogroupe.wecheapis.dtos.responses.CheckRequestStatusResponse
import dev.perogroupe.wecheapis.dtos.responses.ContactResponse
import dev.perogroupe.wecheapis.dtos.responses.FileResponse
import dev.perogroupe.wecheapis.dtos.responses.HelloRetirementResponse
import dev.perogroupe.wecheapis.dtos.responses.NewRequestResponse
import dev.perogroupe.wecheapis.dtos.responses.PendingRequestResponse
import dev.perogroupe.wecheapis.dtos.responses.RejectedRequestResponse
import dev.perogroupe.wecheapis.dtos.responses.StructureResponse
import dev.perogroupe.wecheapis.dtos.responses.UpdateRequestResponse
import dev.perogroupe.wecheapis.dtos.responses.UserResponse
import dev.perogroupe.wecheapis.entities.ApprovedRequest
import dev.perogroupe.wecheapis.entities.CheckRequestStatus
import dev.perogroupe.wecheapis.entities.Contact
import dev.perogroupe.wecheapis.entities.File
import dev.perogroupe.wecheapis.entities.HelloRetirement
import dev.perogroupe.wecheapis.entities.NewRequest
import dev.perogroupe.wecheapis.entities.PendingRequest
import dev.perogroupe.wecheapis.entities.RejectedRequest
import dev.perogroupe.wecheapis.entities.Role
import dev.perogroupe.wecheapis.entities.Structure
import dev.perogroupe.wecheapis.entities.UpdateRequest
import dev.perogroupe.wecheapis.entities.User
import dev.perogroupe.wecheapis.utils.enums.RequestStatus
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class AppConverter {
    fun roleRequestToRole(request: RoleRequest): Role {
        val role = Role()
        return role.copy(
            roleName = request.roleName
        )
    }

    fun userRequestToUser(request: UserRequest): User {
        val user = User()
        return user.copy(
            phoneNumber = request.phoneNumber!!,
            email = request.email!!,
            username = request.serialNumber!!,
            firstname = request.firstname!!,
            lastname = request.lastname!!,
            serialNumber = request.serialNumber,
            birthdate = request.birthdate?.toDate("dd/MM/yyyy")!!
        )
    }

    fun userRequestToUser(request: UserAdminRequest): User {
        val user = User()
        return user.copy(
            phoneNumber = request.phoneNumber!!,
            email = request.email!!,
            username = request.serialNumber!!,
            firstname = request.firstname!!,
            lastname = request.lastname!!,
            serialNumber = request.serialNumber,
            birthdate = request.birthdate?.toDate("dd/MM/yyyy")!!
        )
    }

}

fun StructureRequest.toStructure(): Structure = Structure(
    name = name!!
)

fun User.requestUser(request: UserRequest): User = this.copy(
    username = request.serialNumber!!,
    email = request.email!!,
//        password = request.password!!,
    isNotLocked = true
)

fun Role.requestRole(request: RoleRequest): Role = this.copy(
    roleName = request.roleName
)

fun Role.response(): RoleResponse = RoleResponse(
    id = id,
    roleName = roleName
)

fun File.response(): FileResponse = FileResponse(
    id = id,
    name = name,
    url = url,
    type = type
)

fun User.response(): UserResponse = UserResponse(
    id = id,
    username = username,
    firstname = firstname,
    lastname = lastname,
    phoneNumber = phoneNumber,
    serialNumber = serialNumber,
    birthdate = birthdate,
    email = email,
    roles = roles.map { it.response() },
    avatar = avatar?.response(), // Handle nullable avatar
    profession = profession,
    structure = structure?.response(),
    requestNumber = requestNumber,
    hasRequested = hasRequested
)

fun User.toUpdate(request: UpdateUserRequest): User = this.copy(
    firstname = request.firstname!!,
    lastname = request.lastname!!,
    phoneNumber = request.phoneNumber!!,
    serialNumber = request.serialNumber!!,
    birthdate = request.birthdate?.toDate("dd/MM/yyyy")!!
)

fun Structure.response(): StructureResponse = StructureResponse(
    id = id,
    name = name,
    createdAt = createdAt
)

fun StructureRequest.toStructure(id: UUID): Structure = Structure(
    name = name!!
)

fun ContactRequest.toContact(): Contact = Contact(
    name = name!!,
    phoneNumber = phoneNumber!!,
    message = message!!,
    email = email!!,
    serialNumber = serialNumber!!,
    subject = subject!!
)

fun Contact.response(): ContactResponse = ContactResponse(
    id = id,
    name = name,
    email = email,
    phoneNumber = phoneNumber,
    message = message,
    serialNumber = serialNumber,
    subject = subject
)

fun HelloRetirementRequest.toHelloRetirement(): HelloRetirement = HelloRetirement(
    firstName = firstName!!,
    lastName = lastName!!,
    yearOfDeparture = yearOfDeparture!!,
    phoneNumber = phoneNumber!!,
    email = email!!,
    birthDate = birthDate!!,
    serialNumber = serialNumber!!,
    emergencyContact = emergencyContact!!,
    subject = subject!!,
    message = message!!
)

fun HelloRetirement.response(): HelloRetirementResponse = HelloRetirementResponse(
    id = id,
    firstName = firstName,
    lastName = lastName,
    yearOfDeparture = yearOfDeparture,
    phoneNumber = phoneNumber,
    email = email,
    birthDate = birthDate,
    emergencyContact = emergencyContact,
    subject = subject,
    message = message,
    serialNumber = serialNumber,
    structure = structure?.response(),
    createdAt = createdAt
)

fun NewRequestReq.toNewRequest(): NewRequest = NewRequest(
    firstName = firstName!!,
    lastName = lastName!!,
    civilName = civilName!!,
    startPeriod = startPeriod!!.toDate("dd/MM/yyyy"),
    endPeriod = endPeriod!!.toDate("dd/MM/yyyy"),
    dateOfFirstEntryService = dateOfFirstEntryService!!.toDate("dd/MM/yyyy"),
    firstNameOfPreviousOfficial = firstNameOfPreviousOfficial!!,
    lastNameOfPreviousOfficial = lastNameOfPreviousOfficial!!,
    gradeOfPreviousOfficial = gradeOfPreviousOfficial!!,
    serialNumberOfPreviousOfficial = serialNumberOfPreviousOfficial!!,
    positionHeldOfPreviousOfficial = positionHeldOfPreviousOfficial!!,
    bodyOfPreviousOfficial = bodyOfPreviousOfficial!!,
    requestStatus = RequestStatus.NEW,
)

fun NewRequest.response(): NewRequestResponse = NewRequestResponse(
    id = id,
    firstName = firstName,
    lastName = lastName,
    civilName = civilName,
    startPeriod = startPeriod,
    endPeriod = endPeriod,
    dateOfFirstEntryService = dateOfFirstEntryService,
    firstNameOfPreviousOfficial = firstNameOfPreviousOfficial,
    lastNameOfPreviousOfficial = lastNameOfPreviousOfficial,
    gradeOfPreviousOfficial = gradeOfPreviousOfficial,
    serialNumberOfPreviousOfficial = serialNumberOfPreviousOfficial,
    positionHeldOfPreviousOfficial = positionHeldOfPreviousOfficial,
    bodyOfPreviousOfficial = bodyOfPreviousOfficial,
    appointmentDecree = appointmentDecree?.response(),
    handingOver = handingOver?.response(),
    requestNumber = requestNumber!!,
    structure = structure?.response(),
    requestStatus = requestStatus,
    createdAt = createdAt,
    title = title,
//    user = user?.response()
)

/**
 * Convert a PendingRequest object to a PendingRequestResponse object.
 *
 * @return The PendingRequestResponse object converted from the PendingRequest object.
 */
fun PendingRequest.response(): PendingRequestResponse {
    return PendingRequestResponse(
        id = id,
        firstName = firstName,
        lastName = lastName,
        structure = structure?.response(),
        civilName = civilName,
        createdAt = createdAt,
        startPeriod = startPeriod,
        endPeriod = endPeriod,
        dateOfFirstEntryService = dateOfFirstEntryService,
        firstNameOfPreviousOfficial = firstNameOfPreviousOfficial,
        lastNameOfPreviousOfficial = lastNameOfPreviousOfficial,
        serialNumberOfPreviousOfficial = serialNumberOfPreviousOfficial,
        gradeOfPreviousOfficial = gradeOfPreviousOfficial,
        positionHeldOfPreviousOfficial = positionHeldOfPreviousOfficial,
        bodyOfPreviousOfficial = bodyOfPreviousOfficial,
        requestStatus = requestStatus,
        requestNumber = requestNumber,
        appointmentDecree = appointmentDecree?.response(),
        handingOver = handingOver?.response(),
    )
}


/**
 * Convert a NewRequest object to a PendingRequest object.
 *
 * @return The PendingRequest object converted from the NewRequest object.
 */
fun NewRequest.toPendingRequest(): PendingRequest {
    return PendingRequest(
        firstName = firstName,
        lastName = lastName,
        civilName = civilName,
        startPeriod = startPeriod,
        endPeriod = endPeriod,
        dateOfFirstEntryService = dateOfFirstEntryService,
        firstNameOfPreviousOfficial = firstNameOfPreviousOfficial,
        lastNameOfPreviousOfficial = lastNameOfPreviousOfficial,
        gradeOfPreviousOfficial = gradeOfPreviousOfficial,
        serialNumberOfPreviousOfficial = serialNumberOfPreviousOfficial,
        positionHeldOfPreviousOfficial = positionHeldOfPreviousOfficial,
        bodyOfPreviousOfficial = bodyOfPreviousOfficial,
        requestNumber = requestNumber!!,
        requestStatus = RequestStatus.PENDING,
        structure = structure,
        appointmentDecree = appointmentDecree,
        handingOver = handingOver,
        user = user
    )
}


/**
 * Convert a CheckRequestStatusReq object to a CheckRequestStatus object.
 *
 * @return The CheckRequestStatus object converted from the CheckRequestStatusReq object.
 */
fun CheckRequestStatusReq.toCheckRequestStatus(): CheckRequestStatus = CheckRequestStatus(
    requestNumber = requestNumber,
    requestStatus = requestStatus,
    user = user,
    structure = user.structure,
    comment = comment,
    createByDpafAt = createByDpafAt,
    createdBySupAt = createdBySupAt
)

/**
 * Converts a CheckRequestStatus object to a CheckRequestStatusResponse.
 *
 * @return The CheckRequestStatusResponse object.
 */
fun CheckRequestStatus.response(): CheckRequestStatusResponse = CheckRequestStatusResponse(
    // Copy the id from the CheckRequestStatus object
    id = id,

    // Copy the requestNumber from the CheckRequestStatus object
    requestNumber = requestNumber,

    // Copy the requestStatus from the CheckRequestStatus object
    requestStatus = requestStatus,

    // Convert the user object to a UserResponse object if it exists
    user = user?.response(),

    // Copy the comment from the CheckRequestStatus object
    comment = comment,

    // Convert the structure object to a StructureResponse object if it exists
    structure = structure?.response(),

    // Copy the createdAt from the CheckRequestStatus object
    createdAt = createdAt,

    // Copy the createdBySupAt from the CheckRequestStatus object
    createdBySupAt = createdBySupAt,

    // Copy the createByDpafAt from the CheckRequestStatus object
    createByDpafAt = createByDpafAt
)

/**
 * Convert a RejectedRequest object to a RejectedRequestResponse object.
 *
 * @return The RejectedRequestResponse object converted from the RejectedRequest object.
 */
fun RejectedRequest.response(): RejectedRequestResponse = RejectedRequestResponse(
    id = id,
    requestNumber = requestNumber,
    firstName = firstName,
    lastName = lastName,
    civilName = civilName,
    createdAt = createdAt,
    startPeriod = startPeriod,
    endPeriod = endPeriod,
    dateOfFirstEntryService = dateOfFirstEntryService,
    firstNameOfPreviousOfficial = firstNameOfPreviousOfficial,
    lastNameOfPreviousOfficial = lastNameOfPreviousOfficial,
    serialNumberOfPreviousOfficial = serialNumberOfPreviousOfficial,
    gradeOfPreviousOfficial = gradeOfPreviousOfficial,
    positionHeldOfPreviousOfficial = positionHeldOfPreviousOfficial,
    bodyOfPreviousOfficial = bodyOfPreviousOfficial,
    requestStatus = requestStatus,
    rejectReason = rejectReason,
    reject = reject,
    appointmentDecree = appointmentDecree?.response(),
    handingOver = handingOver?.response(),
)

fun NewRequest.toRejectedRequest(): RejectedRequest = RejectedRequest(
    firstName = firstName,
    lastName = lastName,
    civilName = civilName,
    startPeriod = startPeriod,
    endPeriod = endPeriod,
    dateOfFirstEntryService = dateOfFirstEntryService,
    firstNameOfPreviousOfficial = firstNameOfPreviousOfficial,
    lastNameOfPreviousOfficial = lastNameOfPreviousOfficial,
    serialNumberOfPreviousOfficial = serialNumberOfPreviousOfficial,
    gradeOfPreviousOfficial = gradeOfPreviousOfficial,
    positionHeldOfPreviousOfficial = positionHeldOfPreviousOfficial,
    bodyOfPreviousOfficial = bodyOfPreviousOfficial,
    requestNumber = requestNumber!!,
    requestStatus = RequestStatus.REJECTED,
    rejectReason = "",
    reject = "",
    user = user,
    structure = structure,
    appointmentDecree = appointmentDecree,
    handingOver = handingOver
)

fun PendingRequest.toRejectedRequest(): RejectedRequest = RejectedRequest(
    firstName = firstName,
    lastName = lastName,
    civilName = civilName,
    startPeriod = startPeriod,
    endPeriod = endPeriod,
    dateOfFirstEntryService = dateOfFirstEntryService,
    firstNameOfPreviousOfficial = firstNameOfPreviousOfficial,
    lastNameOfPreviousOfficial = lastNameOfPreviousOfficial,
    serialNumberOfPreviousOfficial = serialNumberOfPreviousOfficial,
    gradeOfPreviousOfficial = gradeOfPreviousOfficial,
    positionHeldOfPreviousOfficial = positionHeldOfPreviousOfficial,
    bodyOfPreviousOfficial = bodyOfPreviousOfficial,
    requestNumber = requestNumber!!,
    requestStatus = RequestStatus.REJECTED,
    rejectReason = "",
    reject = "",
    user = user,
    handingOver = handingOver,
    appointmentDecree = appointmentDecree,
    structure = structure
)

/**
 * Convert a PendingRequest object to an ApprovedRequest object.
 *
 * @return The ApprovedRequest object converted from the PendingRequest object.
 */
fun PendingRequest.toApprovedRequest(): ApprovedRequest {
    // Convert and return the PendingRequest object to an ApprovedRequest object
    return ApprovedRequest(
        firstName = firstName,
        lastName = lastName,
        civilName = civilName,
        startPeriod = startPeriod,
        endPeriod = endPeriod,
        dateOfFirstEntryService = dateOfFirstEntryService,
        firstNameOfPreviousOfficial = firstNameOfPreviousOfficial,
        lastNameOfPreviousOfficial = lastNameOfPreviousOfficial,
        serialNumberOfPreviousOfficial = serialNumberOfPreviousOfficial,
        gradeOfPreviousOfficial = gradeOfPreviousOfficial,
        positionHeldOfPreviousOfficial = positionHeldOfPreviousOfficial,
        bodyOfPreviousOfficial = bodyOfPreviousOfficial,
        requestNumber = requestNumber!!,
        requestStatus = RequestStatus.APPROVED,
        user = user,
        structure = structure,
        appointmentDecree = appointmentDecree,
        handingOver = handingOver,
    )
}

/**
 * Generates a response for an ApprovedRequest.
 *
 * @return ApprovedRequestResponse containing the response details
 */
fun ApprovedRequest.response(): ApprovedRequestResponse {
    // Create a new ApprovedRequestResponse object with the following properties:
    // - id: the id of the ApprovedRequest
    // - requestNumber: the request number of the ApprovedRequest
    // - firstName: the first name of the ApprovedRequest
    // - lastName: the last name of the ApprovedRequest
    // - civilName: the civil name of the ApprovedRequest
    // - createdAt: the creation date of the ApprovedRequest
    // - startPeriod: the start period of the ApprovedRequest
    // - endPeriod: the end period of the ApprovedRequest
    // - dateOfFirstEntryService: the date of first entry service of the ApprovedRequest
    // - firstNameOfPreviousOfficial: the first name of the previous official of the ApprovedRequest
    // - lastNameOfPreviousOfficial: the last name of the previous official of the ApprovedRequest
    // - serialNumberOfPreviousOfficial: the serial number of the previous official of the ApprovedRequest
    // - gradeOfPreviousOfficial: the grade of the previous official of the ApprovedRequest
    // - positionHeldOfPreviousOfficial: the position held by the previous official of the ApprovedRequest
    // - bodyOfPreviousOfficial: the body of the previous official of the ApprovedRequest
    // - requestStatus: the status of the ApprovedRequest
    // - user: the user associated with the ApprovedRequest
    return ApprovedRequestResponse(
        id = id,
        requestNumber = requestNumber,
        firstName = firstName,
        lastName = lastName,
        civilName = civilName,
        createdAt = createdAt,
        startPeriod = startPeriod,
        endPeriod = endPeriod,
        dateOfFirstEntryService = dateOfFirstEntryService,
        firstNameOfPreviousOfficial = firstNameOfPreviousOfficial,
        lastNameOfPreviousOfficial = lastNameOfPreviousOfficial,
        serialNumberOfPreviousOfficial = serialNumberOfPreviousOfficial,
        gradeOfPreviousOfficial = gradeOfPreviousOfficial,
        positionHeldOfPreviousOfficial = positionHeldOfPreviousOfficial,
        bodyOfPreviousOfficial = bodyOfPreviousOfficial,
        requestStatus = requestStatus,
        user = user?.response()
    )
}

/**
 * Converts an UpRequest object to an UpdateRequest object.
 *
 * @return UpdateRequest object with appointmentDecree, handingOver, and reason fields set.
 */
fun UpRequest.toUpdateRequest(): UpdateRequest = UpdateRequest(
    appointmentDecree = appointmentDecree,
    handingOver = handingOver,
    reason = reason
)

/**
 * Generates a response for an Update Request.
 *
 * @return UpdateRequestResponse containing the response details
 */
fun UpdateRequest.response(): UpdateRequestResponse {
    // Create an UpdateRequestResponse object with the required fields
    return UpdateRequestResponse(
        id = id,
        handingOver = handingOver,
        appointmentDecree = appointmentDecree,
        reason = reason,
        request = request?.response()
    )
}