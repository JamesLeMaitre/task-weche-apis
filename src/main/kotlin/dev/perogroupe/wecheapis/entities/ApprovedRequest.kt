package dev.perogroupe.wecheapis.entities

import dev.perogroupe.wecheapis.utils.enums.RequestStatus
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor
import java.io.Serializable
import java.time.Instant
import java.util.Date
import java.util.UUID

@Data
@Entity
@Table(name = "approved_requests")
@NoArgsConstructor
@AllArgsConstructor
data class ApprovedRequest(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String = UUID.randomUUID().toString(),
    val firstName: String,
    val lastName: String,
    @ManyToOne
    @JoinColumn(name = "structure")
    val structure: Structure? = null,
    val civilName: String,
    @Column(name = "created_at")
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
    @Enumerated(EnumType.STRING)
    val requestStatus: RequestStatus,
    @ManyToOne
    @JoinColumn(name = "new_request")
    val newRequest: PendingRequest? = null,
    @ManyToOne
    @JoinColumn(name = "requesting_user")
    val user : User? = null
) : Serializable{

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
        requestStatus = RequestStatus.APPROVED
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val approvedRequest = other as ApprovedRequest
        return id == approvedRequest.id
    }


    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun toString(): String {
        return "ApprovedRequest(id=$id, name='$firstName $lastName')"
    }
}
