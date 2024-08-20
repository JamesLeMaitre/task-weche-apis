package dev.perogroupe.wecheapis.repositories

import dev.perogroupe.wecheapis.entities.CheckRequestStatus
import dev.perogroupe.wecheapis.entities.Structure
import dev.perogroupe.wecheapis.entities.User
import dev.perogroupe.wecheapis.utils.enums.RequestStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.Instant
import java.util.Optional

interface CheckRequestStatusRepository : JpaRepository<CheckRequestStatus, String> {
    fun searchCheckRequestStatusByRequestNumber(requestNumber: String): Optional<CheckRequestStatus>
    fun existsCheckRequestStatusByRequestNumber(requestNumber: String): Boolean

    fun findByRequestNumber(requestNumber: String): Optional<CheckRequestStatus>
    fun findAllByStructureIdAndRequestStatusIsNot(
        structureId: String,
        requestStatus: RequestStatus,
    ): List<CheckRequestStatus>

    // Write repository to get all requests that are approved or rejected
    fun findAllByUserOrderByCreatedAtDesc(user: User): List<CheckRequestStatus>

    fun findAllByStructureIdOrderByCreatedAtDesc(structureId: String): List<CheckRequestStatus>

    @Query("SELECT c FROM CheckRequestStatus c WHERE c.structure.id = ?1 AND c.createByDpafAt < ?2 order by c.createByDpafAt desc")
    fun findAllByRequestStatusIsNotAndAndCreateByDpafAtIsNotNull(structureId: String, createByDpafAt: Instant): List<CheckRequestStatus>

    // Function to search from username or profession
    @Query("SELECT c FROM CheckRequestStatus c WHERE c.user.username = ?1 OR c.user.profession = ?2")
    fun findAllByUser_FirstnameOrUser_Profession(username: String, profession: String): List<CheckRequestStatus>
}