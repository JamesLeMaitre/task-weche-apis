package dev.perogroupe.wecheapis.entities

import dev.perogroupe.wecheapis.utils.enums.RequestStatus
import jakarta.persistence.*
import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor
import java.io.Serializable
import java.time.Instant
import java.util.*


@Data
@Entity
@Table(name = "rejected_requests")
@NoArgsConstructor
@AllArgsConstructor
data class RejectedRequest(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String = UUID.randomUUID().toString(),
//    val firstName: String,
//    val lastName: String,
    @ManyToOne
    @JoinColumn(name = "structure")
    val structure: Structure? = null,
    @Column(name = "created_at")
    val createdAt: Instant = Instant.now(),
//    val startPeriod: Date,
//    val endPeriod: Date,
//    val dateOfFirstEntryService: Date,
    @ManyToOne
    @JoinColumn(name = "appointment_decree")
    val appointmentDecree: File? = null,
    @ManyToOne
    @JoinColumn(name = "handing_over")
    val handingOver: File? = null,
    val requestNumber: String? = null,
//    val firstNameOfPreviousOfficial: String,
//    val lastNameOfPreviousOfficial: String,
//    val serialNumberOfPreviousOfficial: String,
//    val gradeOfPreviousOfficial: String,
//    val positionHeldOfPreviousOfficial: String,
//    val bodyOfPreviousOfficial: String,
    @Enumerated(EnumType.STRING)
    val requestStatus: RequestStatus,
    @ManyToOne
    @JoinColumn(name = "new_request")
    val newRequest: PendingRequest? = null,
    val rejectReason: String,
    val reject: String,
    @ManyToOne
    @JoinColumn(name = "requesting_user")
    val user : User? = null,

    @ManyToOne
    @JoinColumn(name = "beneficiary")
    val beneficiary: Beneficiary? = null,

    val firstName: String? = null,
    val lastName: String? = null,
    val structureId: String? = null,
    val serialNumber: String? = null,
    val body: String? = null, // Corps
    val grade: String? = null, // Grade
    val gradeDate: Date? = null, // Grade date
    val ua: String? = null, // UA
    val positionHeld: String? = null, // Poste de l'agent
//

    // For second select beneficiary
    val firstNameOfPreviousOfficial: String? = null,
    val lastNameOfPreviousOfficial: String? = null,
    val fonction: String? = null,
    val dateFonction: Date? = null,
    val serialNumberOfPreviousOfficial: String? = null,
    // Step 2 attributes
//    val agentPosition: String? = null,
    val ppsDate: Date? = null,
    val uaDate: Date? = null, // Date d'entrée àl'UA
) : Serializable{

    constructor(): this(
        firstName = "",
        lastName = "",
//        startPeriod = Date(),
//        endPeriod = Date(),
//        dateOfFirstEntryService = Date(),
        firstNameOfPreviousOfficial = "",
        lastNameOfPreviousOfficial = "",
        serialNumberOfPreviousOfficial = "",
//        gradeOfPreviousOfficial = "",
//        positionHeldOfPreviousOfficial = "",
//        bodyOfPreviousOfficial = "",
        requestStatus = RequestStatus.REJECTED,
        rejectReason = "",
        reject = "",

        serialNumber = "",
        grade = "",
        gradeDate = Date(),
        ua = "",
        positionHeld = "",
//        agentPosition = "",
        ppsDate = Date(),
        uaDate = Date(),
        fonction = "",
        dateFonction = Date(),
        body = "",
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as RejectedRequest
        return id == other.id
    }

    override fun hashCode(): Int {
        return Objects.hash(id)
    }

    override fun toString(): String {
         return "RejectedRequest(id=$id, reasonOfRejection='$rejectReason', reject='$reject')"
    }



}

