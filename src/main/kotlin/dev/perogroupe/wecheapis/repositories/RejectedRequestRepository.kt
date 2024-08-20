package dev.perogroupe.wecheapis.repositories

import dev.perogroupe.wecheapis.entities.RejectedRequest
import dev.perogroupe.wecheapis.entities.Structure
import dev.perogroupe.wecheapis.utils.enums.RequestStatus
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface RejectedRequestRepository: JpaRepository<RejectedRequest, String> {
    fun countByStructureAndRequestStatusIsNot(structure: Structure?,requestStatus: RequestStatus): Long
    fun findByRequestNumber(number:String) : Optional<RejectedRequest>
}