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
@Table(name = "beneficiaries")
@NoArgsConstructor
@AllArgsConstructor
data class Beneficiary(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val attribute: String,
    @Column(name = "created_at")
    val createdAt: Instant = Instant.now()
): Serializable {
    constructor(): this(
        name = "",
        attribute = ""
    )
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val beneficiary = other as Beneficiary
        return id == beneficiary.id
    }

    override fun toString(): String {
        return "Beneficiary(id=$id, name='$name')"
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + createdAt.hashCode()
        return result
    }

}
