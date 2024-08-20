package dev.perogroupe.wecheapis.repositories

import dev.perogroupe.wecheapis.entities.HelloRetirement
import dev.perogroupe.wecheapis.entities.Structure
import org.springframework.data.jpa.repository.JpaRepository

interface HelloRetirementRepository: JpaRepository<HelloRetirement, String> {

    fun findByStructureId(structure: String): List<HelloRetirement>

    fun findByStructureIdAndYearOfDeparture(structureId: String, year: String): List<HelloRetirement>
}