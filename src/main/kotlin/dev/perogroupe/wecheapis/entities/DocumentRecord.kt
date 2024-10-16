package dev.perogroupe.wecheapis.entities

import jakarta.persistence.*
import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor
import java.io.Serializable
import java.util.*

@Data
@Entity
@Table(name = "document_records")
@NoArgsConstructor
@AllArgsConstructor
data class DocumentRecord(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String = UUID.randomUUID().toString(),
    val createdAt: Date = Date(),
    @Column(name = "request_number", unique = true, nullable = false)
    val requestNumber: String,

    @Column(name = "reference", unique = true, nullable = false)
    val reference: String,

    @Lob  // To store large binary data
    @Column(name = "pdf_data", nullable = false)
    val pdfData: ByteArray
): Serializable {
    constructor(): this(
        createdAt = Date(),
        requestNumber = "",
        reference = "",
        pdfData = ByteArray(0)
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val documentRecord = other as DocumentRecord
        return id == documentRecord.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
