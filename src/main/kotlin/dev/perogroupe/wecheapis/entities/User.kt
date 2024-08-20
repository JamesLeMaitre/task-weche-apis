package dev.perogroupe.wecheapis.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import lombok.AllArgsConstructor
import lombok.NoArgsConstructor
import lombok.Data
import org.hibernate.annotations.NaturalId
import java.io.Serializable
import java.time.Instant
import java.util.Date
import java.util.UUID

@Data
@Entity
@Table(name = "users", uniqueConstraints = [UniqueConstraint(columnNames = ["username", "email"])])
@NoArgsConstructor
@AllArgsConstructor
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String = UUID.randomUUID().toString(),
    val firstname: String = "",
    val lastname: String = "",
    // In our case, we will use the serial number as username
    val username: String = "",
    val serialNumber: String = "",
    val phoneNumber: String = "",
    @JsonIgnore
    var password: String = "",
    @NaturalId
    val email: String = "",
    var isNotLocked: Boolean = false,
    val birthdate: Date = Date(),
    val createdAt: Instant = Instant.now(),
    val profession: String = "",
    @ManyToOne
    @JoinColumn(name = "structure")
    var structure: Structure? = null,
    @ManyToOne
    @JoinColumn(name = "avatar")
    val avatar: File? = null,
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "users_roles",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "role_id", referencedColumnName = "id")]
    )
    val roles: MutableList<Role> = mutableListOf(),
    var requestNumber: String? = null,
    var hasRequested: Boolean = false
) : Serializable {

    constructor() : this(
        username = "",
        serialNumber = "",
        birthdate = Date(),
        password = "",
        firstname = "",
        lastname = "",
        phoneNumber = "",
        profession = "",
        email = "",
        createdAt = Instant.now(),
        isNotLocked = false,
        avatar = null
    )

    /**
     * Checks if this User object is equal to another object based on the ID.
     *
     * @param other The object to compare to.
     * @return True if the objects are equal based on ID, false otherwise.
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        val user = other as User

        return id == user.id // Use only the ID for equality check
    }

    /**
     * Returns the hash code of the ID of this User object.
     *
     * @return the hash code of the ID.
     */
    override fun hashCode(): Int {
        // Return the hash code of the ID.
        return id.hashCode()
    }

    override fun toString(): String {
        return "User(id='$id', username='$username', email='$email', hasRequested='$hasRequested', requestNumber='$requestNumber')"
    }
}