package dev.perogroupe.wecheapis.dtos.requests

import lombok.Data
import lombok.ToString
import java.io.Serializable

/**
 * Request for storing a document record.
 */

@Data
@ToString
data class DocumentRecordRequest(
    val requestNumber: String,
    val reference: String,
    val pdfData: ByteArray
) : Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DocumentRecordRequest

        if (requestNumber != other.requestNumber) return false
        if (reference != other.reference) return false
        if (!pdfData.contentEquals(other.pdfData)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = requestNumber.hashCode()
        result = 31 * result + reference.hashCode()
        result = 31 * result + pdfData.contentHashCode()
        return result
    }

}
