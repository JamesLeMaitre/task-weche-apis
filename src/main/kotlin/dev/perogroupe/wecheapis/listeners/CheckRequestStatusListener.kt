package dev.perogroupe.wecheapis.listeners

import dev.perogroupe.wecheapis.dtos.requests.CheckRequestStatusReq
import dev.perogroupe.wecheapis.events.CheckRequestStatusEvent
import dev.perogroupe.wecheapis.exceptions.UserNotFoundException
import dev.perogroupe.wecheapis.repositories.UserRepository
import dev.perogroupe.wecheapis.services.CheckRequestStatusService
import dev.perogroupe.wecheapis.utils.enums.RequestStatus
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class CheckRequestStatusListener(
    private val service: CheckRequestStatusService,
    private val userRepository: UserRepository,
) {

    companion object {
        val log: Logger = LoggerFactory.getLogger(CheckRequestStatusListener::class.java)
    }

    @EventListener
    fun handleCustomEvent(event: CheckRequestStatusEvent) {
        task(event.checkRequestStatusReq)
    }

    private fun task(value: CheckRequestStatusReq) {
        val user = userRepository.findByUsername(value.user.username)
            .orElseThrow { UserNotFoundException("User not found") }
        user.requestNumber = value.requestNumber
        if (value.requestStatus === RequestStatus.APPROVED) {
            user.hasRequested = false
        } else if (value.requestStatus === RequestStatus.REJECTED) {
            user.hasRequested = false
        } else if(value.requestStatus === RequestStatus.UPDATE) {
            user.hasRequested = true
        } else if(value.requestStatus === RequestStatus.NEW) {
            user.hasRequested = true
        }
        userRepository.save(user)
        service.store(value)
    }
}