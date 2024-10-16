package dev.perogroupe.wecheapis.entities

import jakarta.persistence.*
import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor
import java.io.Serializable
import java.time.Instant
import java.util.*

@Data
@Entity
@Table(name = "dnrs")
@NoArgsConstructor
@AllArgsConstructor
data class Dnr(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String = UUID.randomUUID().toString(),
    @ManyToOne
    @JoinColumn(name = "user_request")
    val user: User? = null,
    val validityStart: Date,
    val validityEnd: Date,
    @ManyToOne
    @JoinColumn(name = "new_request")
    val newRequest: NewRequest? = null,
    var valid: Boolean,
    val createdAt: Instant = Instant.now(),
) : Serializable {

    constructor() : this(
        validityStart = Date(),
        validityEnd = Date(),
        valid = false
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val dnr = other as Dnr
        return id == dnr.id
    }

    override fun toString(): String {
        return "Dnr(id=$id, user='${user?.id}', validityStart=$validityStart, validityEnd=$validityEnd, newRequest=${newRequest?.id}, valid=$valid)"
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + user.hashCode()
        result = 31 * result + validityStart.hashCode()
        result = 31 * result + validityEnd.hashCode()
        result = 31 * result + newRequest.hashCode()
        result = 31 * result + valid.hashCode()
        result = 31 * result + createdAt.hashCode()
        return result
    }
}
