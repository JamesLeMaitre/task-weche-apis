package dev.perogroupe.wecheapis.entities

import dev.perogroupe.wecheapis.utils.enums.FileType
import jakarta.persistence.*
import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor
import java.io.Serializable
import java.util.*

@Data
@Entity
@Table(name = "file_counters")
@NoArgsConstructor
@AllArgsConstructor
data class FileCounter(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String = UUID.randomUUID().toString(),
    val count: Int,
    val reference: String,
    val createdAt: Date = Date(),
    val fileType: FileType
) : Serializable {
    constructor() : this(
        count = 0,
        reference = "",
        fileType = FileType.APP_DOCUMENT
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val fileCounter = other as FileCounter
        return id == fileCounter.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
