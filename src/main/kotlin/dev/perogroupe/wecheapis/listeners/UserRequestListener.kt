package dev.perogroupe.wecheapis.listeners

import dev.perogroupe.wecheapis.events.DnrEvent
import dev.perogroupe.wecheapis.repositories.ApprovedRequestRepository
import dev.perogroupe.wecheapis.repositories.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class UserRequestListener(
    private val repository: UserRepository,
    private val approvedRequestRepository: ApprovedRequestRepository
) {
   /* private val executorService: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()

    companion object {
        val log: org.slf4j.Logger = LoggerFactory.getLogger(UserRequestListener::class.java)
    }

    @EventListener()
    fun handleCustomEvent() = run {
       // Check if the approved request for each user is expired
        executorService.schedule({
            // TODO
        }, 10, TimeUnit.MINUTES)
    }*/
}