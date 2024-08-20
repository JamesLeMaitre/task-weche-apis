package dev.perogroupe.wecheapis.repositories

import dev.perogroupe.wecheapis.entities.ApprovedRequest
import dev.perogroupe.wecheapis.entities.Structure
import dev.perogroupe.wecheapis.utils.enums.RequestStatus
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface ApprovedRequestRepository : JpaRepository<ApprovedRequest, String> {
    fun countByStructureAndRequestStatusIsNot(structure: Structure?, requestStatus: RequestStatus): Long

    fun findByRequestNumber(requestNumber: String): Optional<ApprovedRequest>
}