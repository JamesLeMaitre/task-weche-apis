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
@Table(name = "check_validates")
@NoArgsConstructor
@AllArgsConstructor
data class CheckValidity(
    /**
     * The id of the check validity. It is a UUID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String = UUID.randomUUID().toString(),

    /**
     * The application document. It is a boolean value.
     */
    val appDocument: Boolean = false,
    /**
     * The date of the application document. It is a Instant.
     */
    val appDocumentDateDelivery: Instant,
    val appDocumentDateEnd: Date,

    /**
     * The application for the DNR. It is a boolean value.
     */
    val appDnr: Boolean = false,

    /**
     * The date of the application for the DNR. It is a Instant.
     */
    val appDnrDateDelivery: Instant,
    val appDnrDateEnd: Date,

    /**
     * The user who requested the check validity. It is a User.
     */
    @ManyToOne
    @JoinColumn(name = "requesting_user")
    val user: User? = null,

    /**
     * The date and time when the check validity was created. It is a Instant.
     */
    val createdAt: Instant = Instant.now(),

    /**
     * The date and time when the check validity was last updated. It is a Instant.
     */
    val used: Boolean = false
) : Serializable {

//    constructor() : this(
//        user = User(),
//        createdAt = Instant.now(),
//        used = false
//    )

    override fun toString(): String {
        return "CheckValidity(id='$id', user=$user.firstName, createdAt=$createdAt, used=$used)"
    }

}
