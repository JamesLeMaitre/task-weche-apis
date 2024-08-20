package dev.perogroupe.wecheapis.entities

import jakarta.persistence.Id
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Entity
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor
import java.io.Serializable
import java.util.UUID

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
    var read: Boolean = false
): Serializable{

    constructor(): this(
        user = User(),
        message = "",
        read = false
    )

    override fun toString(): String {
        return "Notifications(id='$id', user=$user.firstName, message='$message')"
    }

}
