package dev.perogroupe.wecheapis.entities

import jakarta.persistence.*
import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor
import java.io.Serializable
import java.util.*

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
data class Notifications(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String = UUID.randomUUID().toString(),
    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User,
    val message: String,
    var read: Boolean = false,
    var readDate: Date = Date()
) : Serializable {

    constructor() : this(
        user = User(),
        message = "",
        read = false
    )

    override fun toString(): String {
        return "Notifications(id='$id', user=$user.firstName, message='$message')"
    }

}
