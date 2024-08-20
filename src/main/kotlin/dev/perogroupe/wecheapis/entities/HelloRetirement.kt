package dev.perogroupe.wecheapis.entities

import dev.perogroupe.wecheapis.dtos.responses.StructureResponse
import jakarta.persistence.Entity
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
@Table(name = "hello_retirements")
@NoArgsConstructor
@AllArgsConstructor
data class HelloRetirement(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String = UUID.randomUUID().toString(),
    val firstName: String,
    val lastName: String,
    val yearOfDeparture: String,
    val phoneNumber: String,
    val serialNumber: String,
    val email: String,
    val birthDate: String,
    val emergencyContact: String,
    val subject: String,
    val message: String,
    @ManyToOne
    @JoinColumn(name = "structure")
    val structure: Structure? = null,
    val createdAt: Instant = Instant.now()
) : Serializable {

    constructor(): this(
        firstName = "",
        lastName = "",
        yearOfDeparture = "",
        phoneNumber = "",
        email = "",
        birthDate = "",
        serialNumber = "",
        emergencyContact = "",
        subject = "",
        message = ""
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        val helloRetirement = other as HelloRetirement

        return id == helloRetirement.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun toString(): String {
        return "HelloRetirement(id=$id, name='$firstName $lastName', yearOfDeparture=$yearOfDeparture)"
    }

}
