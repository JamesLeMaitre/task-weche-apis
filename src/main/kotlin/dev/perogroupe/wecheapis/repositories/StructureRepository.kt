package dev.perogroupe.wecheapis.repositories

import dev.perogroupe.wecheapis.entities.Structure
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface StructureRepository : JpaRepository<Structure,String> {
    fun findByName(name: String) : Optional<Structure>
}