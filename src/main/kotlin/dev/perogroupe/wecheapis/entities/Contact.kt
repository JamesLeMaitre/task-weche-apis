package dev.perogroupe.wecheapis.entities

import jakarta.persistence.Column
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
@Table(name = "contacts")
@NoArgsConstructor
@AllArgsConstructor
data class Contact(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val subject: String,
    val email: String,
    @Column(columnDefinition = "TEXT")
    val message: String,
    val serialNumber: String,
    val phoneNumber: String,
    @Column(name = "created_at")
    val createdAt: Instant = Instant.now()
) : Serializable{

    constructor(): this(
        name = "",
        subject = "",
        email = "",
        message = "",
        serialNumber = "",
        phoneNumber = ""
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val contact = other as Contact
        return id == contact.id
    }

    override fun toString(): String {
        return "Contact(id=$id, name='$name', subject='$subject', message='$message', serialNumber='$serialNumber', phoneNumber='$phoneNumber')"
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + subject.hashCode()
        result = 31 * result + email.hashCode()
        result = 31 * result + message.hashCode()
        result = 31 * result + serialNumber.hashCode()
        result = 31 * result + phoneNumber.hashCode()
        result = 31 * result + createdAt.hashCode()
        return result
    }
}
