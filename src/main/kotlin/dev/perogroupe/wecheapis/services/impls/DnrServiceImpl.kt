package dev.perogroupe.wecheapis.services.impls

import dev.perogroupe.wecheapis.dtos.requests.DnrRequest
import dev.perogroupe.wecheapis.dtos.responses.DnrResponse
import dev.perogroupe.wecheapis.events.DnrEvent
import dev.perogroupe.wecheapis.exceptions.NewRequestNotFoundException
import dev.perogroupe.wecheapis.repositories.DnrRepository
import dev.perogroupe.wecheapis.repositories.NewRequestRepository
import dev.perogroupe.wecheapis.repositories.UserRepository
import dev.perogroupe.wecheapis.services.DnrService
import dev.perogroupe.wecheapis.utils.response
import dev.perogroupe.wecheapis.utils.toDnr
import org.springframework.context.ApplicationEventPublisher
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service

@Service
class DnrServiceImpl(
    private val repository: DnrRepository,
    private val newRequestRepository: NewRequestRepository,
    private val userRepository: UserRepository,
    private val eventPublisher: ApplicationEventPublisher,
) : DnrService {

    /**
     * Create a new DNR
     * @param request the request to create the DNR
     * @return the response of the DNR
     */
    override fun createDnr(request: DnrRequest): DnrResponse {
        val newRequest = newRequestRepository.findByRequestNumber(request.requestNumber)
            .orElseThrow { NewRequestNotFoundException("New request not found") }
        val dnr = request.toDnr().copy(user = request.user, newRequest = newRequest)
        //Publish an event for the DNR
        eventPublisher.publishEvent(DnrEvent(this))
        return repository.save(dnr).response()
    }

    /**
     * Check if the DNR is valid
     * @param authentication the user's authentication
     * @return true if the DNR is valid, false otherwise
     */
    override fun checkDnrValidity(authentication: Authentication): Boolean {
        val user = userRepository.findByUsername(authentication.name)
            .orElseThrow { IllegalArgumentException("User not found") }
        return repository.findByUser(user).last().valid

    }
}