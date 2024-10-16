package dev.perogroupe.wecheapis.repositories

import dev.perogroupe.wecheapis.entities.Beneficiary
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface BeneficiaryRepository : JpaRepository<Beneficiary, String> {
    fun findByName(name: String): Optional<Beneficiary>
    fun findByAttribute(attribute: String): Optional<Beneficiary>
}