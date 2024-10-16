package dev.perogroupe.wecheapis.repositories

import dev.perogroupe.wecheapis.entities.NewRequest
import dev.perogroupe.wecheapis.entities.Structure
import dev.perogroupe.wecheapis.utils.enums.RequestStatus
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.Optional

interface NewRequestRepository: JpaRepository<NewRequest, String> {
//    @Deprecated("Use findByRequestNumber instead")
//    fun findAllByStructureIdAndRequestStatusIs(structureId: String, requestStatus: RequestStatus): List<NewRequest>

    @Query("SELECT r FROM NewRequest r WHERE r.structure.id = :structureId AND r.requestStatus = :requestStatus")
    fun findAllByStructureIdAndRequestStatusIs(structureId: String, requestStatus: RequestStatus): List<NewRequest>

    fun findAllByStructureId(structureId: String, pageable: Pageable): Page<NewRequest>

    fun countByStructureAndRequestStatusIsNot(structure: Structure?, requestStatus: RequestStatus): Long

    fun findByRequestNumber(requestNumber: String): Optional<NewRequest>
}