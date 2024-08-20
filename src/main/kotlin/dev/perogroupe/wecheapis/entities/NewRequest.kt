package dev.perogroupe.wecheapis.entities

import dev.perogroupe.wecheapis.utils.enums.RequestStatus
import jakarta.persistence.*
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Table
import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor
import java.io.Serializable
import java.time.Instant
import java.util.Date
import java.util.UUID

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "new_requests")
data class NewRequest(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String = UUID.randomUUID().toString(),
    val firstName: String,
    val lastName: String,
    @ManyToOne
    @JoinColumn(name = "structure")
    val structure: Structure? = null,
    val civilName: String,
    val createdAt: Instant = Instant.now(),
    val startPeriod: Date,
    val endPeriod: Date,
    val dateOfFirstEntryService: Date,
    @ManyToOne
    @JoinColumn(name = "appointment_decree")
    val appointmentDecree: File? = null,
    @ManyToOne
    @JoinColumn(name = "handing_over")
    val handingOver: File? = null,
    val requestNumber: String? = null,
    val firstNameOfPreviousOfficial: String,
    val lastNameOfPreviousOfficial: String,
    val serialNumberOfPreviousOfficial: String,
    val gradeOfPreviousOfficial: String,
    val positionHeldOfPreviousOfficial: String,
    val bodyOfPreviousOfficial: String,
    val title: String = "Attestation de présence au poste",
    @Enumerated(EnumType.STRING)
    val requestStatus: RequestStatus,
    @ManyToOne
    @JoinColumn(name = "requesting_user")
    val user : User? = null
) : Serializable {

    constructor(): this(
        firstName = "",
        lastName = "",
        civilName = "",
        startPeriod = Date(),
        endPeriod = Date(),
        dateOfFirstEntryService = Date(),
        firstNameOfPreviousOfficial = "",
        lastNameOfPreviousOfficial = "",
        serialNumberOfPreviousOfficial = "",
        gradeOfPreviousOfficial = "",
        positionHeldOfPreviousOfficial = "",
        bodyOfPreviousOfficial = "",
        requestStatus = RequestStatus.NEW,
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val newRequest = other as NewRequest
        return id == newRequest.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun toString(): String {
        return "NewRequest(id=$id, name='$firstName $lastName')"
    }
}
