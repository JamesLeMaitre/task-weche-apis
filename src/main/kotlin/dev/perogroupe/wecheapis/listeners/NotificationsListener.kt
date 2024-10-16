package dev.perogroupe.wecheapis.listeners

import dev.perogroupe.wecheapis.repositories.NotificationsRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

@Component
class NotificationsListener(
    private  val repository: NotificationsRepository
) {

//    private val executorService: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()
//
//    companion object {
//        val log: Logger = LoggerFactory.getLogger(DnrListener::class.java)
//    }
//
//    @EventListener
//    fun handleCustomEvent() = run {
//        task()
//    }

    // Delete notifications read after 1 day
//    private fun task() {
//        executorService.schedule({
//            val notifications = repository.findByRead(true)
//            if (notifications.isNotEmpty()) {
//                notifications.forEach { notification ->
//                    if (notification.readDate.time < System.currentTimeMillis()) {
//                        repository.delete(notification)
//                        log.info("NotificationsListener: Notification for user ${notification.user.firstname} ${notification.user.lastname} has expired")
//                    }
//                }
//            }
//        }, 1, TimeUnit.DAYS)
//    }

}