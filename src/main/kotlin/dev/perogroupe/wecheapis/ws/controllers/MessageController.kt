package dev.perogroupe.wecheapis.ws.controllers

import dev.perogroupe.wecheapis.ws.models.Message
import dev.perogroupe.wecheapis.ws.models.ResponseMessage
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.util.HtmlUtils

@RestController
class MessageController {

    @MessageMapping("/notification")
    @SendTo("/topic/notification")
    @Throws(InterruptedException::class)
    fun getMessage(message: Message): ResponseMessage {
        Thread.sleep(1000) // simulated delay
        return ResponseMessage(HtmlUtils.htmlEscape(message.to), HtmlUtils.htmlEscape(message.text))
    }

}