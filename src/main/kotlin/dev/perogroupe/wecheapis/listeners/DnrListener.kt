package dev.perogroupe.wecheapis.listeners

import dev.perogroupe.wecheapis.events.DnrEvent
import dev.perogroupe.wecheapis.repositories.DnrRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

@Component
class DnrListener(private val repository: DnrRepository) {
    private val executorService: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()

    companion object {
        val log: Logger = LoggerFactory.getLogger(DnrListener::class.java)
    }

    @EventListener
    fun handleCustomEvent(event: DnrEvent) = run {
        task()
    }

    private fun task() {
        executorService.schedule({
            val dnrList = repository.findByValid(true)
            if (dnrList.isNotEmpty()) {
                dnrList.forEach { dnr ->
                    if (dnr.validityEnd.time < System.currentTimeMillis()) {
                        dnr.valid = false
                        repository.save(dnr)
                        log.info("DnrListener: DNR for user ${dnr.user?.firstname} ${dnr.user?.lastname} has expired")
                    }
                }
            }
        }, 1, TimeUnit.MILLISECONDS)
    }
}