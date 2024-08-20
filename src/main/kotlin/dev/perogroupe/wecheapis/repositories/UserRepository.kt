package dev.perogroupe.wecheapis.repositories


import dev.perogroupe.wecheapis.entities.Structure
import dev.perogroupe.wecheapis.entities.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.Optional

interface UserRepository : JpaRepository<User, String> {
    fun findByUsername(username: String): Optional<User>

    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.roleName = ?1 AND u.structure = ?2")
    fun findUserByStructureAndRole(roleName: String, structure: Structure): Optional<User>


    //    fun findByEmail(email: String) : Optional<User>
    fun existsUserByEmail(email: String): Boolean

    fun findFirstByEmail(email: String): Optional<User>
}