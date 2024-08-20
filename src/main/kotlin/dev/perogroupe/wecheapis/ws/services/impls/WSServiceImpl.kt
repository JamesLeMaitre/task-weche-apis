package dev.perogroupe.wecheapis.ws.services.impls

import dev.perogroupe.wecheapis.ws.services.WSService
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service

@Service
class WSServiceImpl(
    private val messageTemplate: SimpMessagingTemplate,
) : WSService {
    override fun sendMessage(message: String) {
        messageTemplate.convertAndSend("/topic/notification", message)
    }
}