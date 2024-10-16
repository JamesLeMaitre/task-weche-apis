package dev.perogroupe.wecheapis.listeners

import dev.perogroupe.wecheapis.services.CheckValidityService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService

class CheckValidityListener(
    private val checkValidityService: CheckValidityService
) {
    private val executorService: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()

    companion object {
        val log: Logger = LoggerFactory.getLogger(DnrListener::class.java)
    }

    // App validity 3 month
    // DNR validity 1 month
}