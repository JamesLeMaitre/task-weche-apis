package dev.perogroupe.wecheapis.repositories

import dev.perogroupe.wecheapis.entities.NewRequest
import dev.perogroupe.wecheapis.entities.PendingRequest
import dev.perogroupe.wecheapis.entities.UpdateRequest
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface UpdateRequestRepository :JpaRepository<UpdateRequest,String> {
    fun findByRequest(pendingReq: PendingRequest): Optional<UpdateRequest>
    fun findByNewRequest(newReq: NewRequest): Optional<UpdateRequest>
    fun findAllByRequest(pendingReq: PendingRequest): List<UpdateRequest>
    fun findAllByNewRequest(newReq: NewRequest): List<UpdateRequest>

}