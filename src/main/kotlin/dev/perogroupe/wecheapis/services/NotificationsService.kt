package dev.perogroupe.wecheapis.services

import dev.perogroupe.wecheapis.entities.Notifications
import org.springframework.security.core.Authentication

interface NotificationsService {
    fun store(request: Notifications): Notifications
    fun read(id: String): Notifications
    fun listNotReadByUser(authentication: Authentication): List<Notifications>
}