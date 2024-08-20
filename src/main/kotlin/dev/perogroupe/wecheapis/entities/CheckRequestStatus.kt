package dev.perogroupe.wecheapis.entities

import dev.perogroupe.wecheapis.utils.enums.RequestStatus
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
import java.util.UUID

@Data
@Entity
@Table(name = "check_request_statuses")
@NoArgsConstructor
@AllArgsConstructor
data class CheckRequestStatus(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String = UUID.randomUUID().toString(),
    val requestNumber: String,
    @Enumerated(EnumType.STRING)
    var requestStatus: RequestStatus,
    val createdAt: Instant = Instant.now(),
    var createdBySupAt: Instant? = null,
    val createByDpafAt: Instant? = null,
    @ManyToOne
    @JoinColumn(name = "requesting_user")
    val user: User? = null,
    val comment: String? = null,
    @ManyToOne
    @JoinColumn(name = "structure_id")
    val structure: Structure? = null
) : Serializable {
    constructor() : this(
        requestNumber = "",
        requestStatus = RequestStatus.PENDING,
        comment = ""
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val checkRequestStatus = other as CheckRequestStatus
        return id == checkRequestStatus.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun toString(): String {
        return "CheckRequestStatus(id=$id, requestNumber='$requestNumber', requestStatus=$requestStatus, createdAt=$createdAt)"
    }
}
