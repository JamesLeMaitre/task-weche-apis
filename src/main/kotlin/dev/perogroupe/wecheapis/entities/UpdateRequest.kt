package dev.perogroupe.wecheapis.entities

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Table
import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import java.io.Serializable
import java.time.Instant
import java.util.UUID

@Data
@Entity
@Table(name = "update_requests")
@NoArgsConstructor
@AllArgsConstructor
data class UpdateRequest(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String = UUID.randomUUID().toString(),
    val appointmentDecree: Boolean,
    val handingOver: Boolean,
    val reason: String,
    @ManyToOne
    @JoinColumn(name = "pending_request_id")
    var request: PendingRequest? = null,
    @ManyToOne
    @JoinColumn(name = "new_request_id")
    var newRequest: NewRequest? = null,
    val createAt: Instant = Instant.now()
): Serializable{
    constructor(): this(
        appointmentDecree = false,
        handingOver = false,
        reason = ""
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        val request = other as UpdateRequest

        return id == request.id // Use only the ID for equality check
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun toString(): String {
        return "UpdateRequest(id='$id', appointmentDecree='$appointmentDecree', handingOver='$handingOver', reason='$reason')"
    }
}
