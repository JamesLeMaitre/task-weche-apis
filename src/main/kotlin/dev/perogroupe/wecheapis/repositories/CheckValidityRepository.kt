package dev.perogroupe.wecheapis.repositories

import dev.perogroupe.wecheapis.entities.CheckValidity
import dev.perogroupe.wecheapis.entities.NewRequest
import dev.perogroupe.wecheapis.entities.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface CheckValidityRepository: JpaRepository<CheckValidity, String> {
    fun findByUserAndUsed(user: User, used: Boolean): List<CheckValidity>
}