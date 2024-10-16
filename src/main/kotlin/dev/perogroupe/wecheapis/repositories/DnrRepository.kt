package dev.perogroupe.wecheapis.repositories

import dev.perogroupe.wecheapis.entities.Dnr
import dev.perogroupe.wecheapis.entities.User
import org.springframework.data.jpa.repository.JpaRepository

interface DnrRepository : JpaRepository<Dnr, String> {
    fun findByUser(user: User): List<Dnr>
    fun findByValid(valid: Boolean): List<Dnr>
}