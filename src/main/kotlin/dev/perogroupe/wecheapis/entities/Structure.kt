package dev.perogroupe.wecheapis.entities

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor
import java.io.Serializable
import java.time.Instant
import java.util.UUID

@Data
@Entity
@Table(name = "structures")
@NoArgsConstructor
@AllArgsConstructor
data class Structure(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val createdAt: Instant = Instant.now(),
): Serializable {
    constructor() : this(
        name = ""
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val structure = other as Structure
        return id == structure.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun toString(): String {
        return "Structure(id=$id, name='$name')"
    }
}
