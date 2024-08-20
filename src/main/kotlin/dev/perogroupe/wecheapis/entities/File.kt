package dev.perogroupe.wecheapis.entities

import jakarta.persistence.*
import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor
import java.io.Serializable
import java.time.Instant
import java.util.UUID

@Data
@Entity
@Table(name = "files")
@NoArgsConstructor
@AllArgsConstructor
data class File(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val url: String = "",
    val type: String = "",
    val createdAt: Instant = Instant.now(),
) : Serializable {

    constructor(): this(
        name = "",
        url = "",
        type = ""
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        val file = other as File

        return id == file.id // Use only the ID for equality check
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun toString(): String {
        return "File(id='$id', name='$name', url='$url', type='$type')"
    }
}