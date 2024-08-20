package dev.perogroupe.wecheapis.repositories

import dev.perogroupe.wecheapis.entities.Role
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface RoleRepository : JpaRepository<Role,String> {
    fun findByRoleName(roleName: String) : Optional<Role>
    fun findAllByRoleName(roleName: String): List<Role>
}