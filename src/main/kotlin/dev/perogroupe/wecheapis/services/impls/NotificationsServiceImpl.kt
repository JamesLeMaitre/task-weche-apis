package dev.perogroupe.wecheapis.services.impls

import dev.perogroupe.wecheapis.entities.Notifications
import dev.perogroupe.wecheapis.exceptions.UserNotFoundException
import dev.perogroupe.wecheapis.repositories.NotificationsRepository
import dev.perogroupe.wecheapis.repositories.UserRepository
import dev.perogroupe.wecheapis.services.NotificationsService
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import java.util.*
import kotlin.NoSuchElementException

@Service
class NotificationsServiceImpl(
    private val repository: NotificationsRepository,
    private val userRepository: UserRepository
): NotificationsService {

    /**
     * Store a new Notification in the repository.
     *
     * @param request The Notification to be stored.
     * @return The stored Notification.
     * @throws UserNotFoundException If the user associated with the Notification is not found.
     */
    override fun store(request: Notifications): Notifications {
        // Save the Notification in the repository
        return repository.save(request)
    }


    /**
     * Read a notification by its ID.
     *
     * @param id The ID of the notification to read.
     * @return The read notification.
     * @throws NoSuchElementException If the notification with the given ID is not found.
     */
    override fun read(id: String): Notifications {
        // Find the notification by its ID
        val notification = repository.findById(id)
            .orElseThrow { NoSuchElementException("Notification with ID $id not found") }

        // Mark the notification as read
        notification.read = true
        notification.readDate = Date()

        // Save the updated notification
        return repository.save(notification)
    }


    /**
     * Lists notifications that have not been read by a specific user.
     *
     * @param authentication The authentication object containing the user's information.
     * @return The list of notifications not read by the user.
     * @throws UserNotFoundException If the user with the provided information is not found.
     */
    override fun listNotReadByUser(authentication: Authentication): List<Notifications> {
        // Find the user by their username
        val user = userRepository.findByUsername(authentication.name).orElseThrow {
            UserNotFoundException("User with this serial number ${authentication.name} not found")
        }

        // Retrieve all unread notifications for the user and order them by ID in descending order
        return repository.findAllByUserAndReadOrderByIdDesc(user, false)
    }
}