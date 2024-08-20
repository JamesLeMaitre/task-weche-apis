package dev.perogroupe.wecheapis.repositories

import dev.perogroupe.wecheapis.entities.Notifications
import dev.perogroupe.wecheapis.entities.User
import org.springframework.data.jpa.repository.JpaRepository

interface NotificationsRepository: JpaRepository<Notifications, String> {
    fun findByUser(user: User): List<Notifications>

    fun findAllByUserAndReadOrderByIdDesc(user: User, isRead: Boolean): List<Notifications>
}