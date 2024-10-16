package dev.perogroupe.wecheapis.repositories

import dev.perogroupe.wecheapis.entities.PendingRequest
import dev.perogroupe.wecheapis.entities.Structure
import dev.perogroupe.wecheapis.utils.enums.RequestStatus
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.Optional

interface PendingRequestRepository: JpaRepository<PendingRequest, String> {
    @Query("SELECT p FROM PendingRequest p WHERE p.structure.id = :structureId AND p.requestStatus != :requestStatus ORDER BY p.createdAt DESC")
    fun findAllByStructureIdAndRequestStatusIsNot(structureId: String,requestStatus: RequestStatus,sort: Sort): List<PendingRequest>
    fun countByStructureAndRequestStatusIsNot(structure: Structure?,requestStatus: RequestStatus): Long
    fun findByRequestNumber(number:String) : Optional<PendingRequest>
}