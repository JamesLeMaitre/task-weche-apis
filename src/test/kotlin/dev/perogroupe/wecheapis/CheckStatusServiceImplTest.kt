package dev.perogroupe.wecheapis

import dev.perogroupe.wecheapis.entities.CheckRequestStatus
import dev.perogroupe.wecheapis.repositories.CheckRequestStatusRepository
import dev.perogroupe.wecheapis.repositories.UserRepository
import dev.perogroupe.wecheapis.services.impls.CheckRequestStatusServiceImpl
import dev.perogroupe.wecheapis.utils.enums.RequestStatus
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import java.time.Instant

class CheckRequestStatusServiceImplTest {

    private val repository = Mockito.mock(CheckRequestStatusRepository::class.java)
    private val userRepository = Mockito.mock(UserRepository::class.java)
    private val service = CheckRequestStatusServiceImpl(userRepository, repository)
    @Test
    fun `test listRequestForDpaf returns non-empty list`() {
        // Mock the repository behavior to return a list of requests with the specified conditions
        `when`(repository.findAllByRequestStatusIsNotAndAndCreateByDpafAtIsNotNull("91055213-4fae-4996-ad8d-eac96f39ba20", Instant.now()))
            .thenReturn(listOf(CheckRequestStatus(requestNumber = "IRAOR0DY", requestStatus = RequestStatus.NEW, createByDpafAt = Instant.now())))

        // Call the function and assert that it returns a non-empty list
        val result = service.listRequestForDpaf("structureId")
        assertEquals(1, result.size)
    }

    @Test
    fun `test listRequestForDpaf returns empty list`() {
        // Mock the repository behavior to return an empty list
        `when`(repository.findAllByRequestStatusIsNotAndAndCreateByDpafAtIsNotNull("structureId", Instant.now()))
            .thenReturn(emptyList())

        // Call the function and assert that it returns an empty list
        val result = service.listRequestForDpaf("structureId")
        assertEquals(0, result.size)
    }
}