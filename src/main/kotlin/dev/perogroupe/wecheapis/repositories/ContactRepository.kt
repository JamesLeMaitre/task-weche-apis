package dev.perogroupe.wecheapis.repositories

import dev.perogroupe.wecheapis.entities.Contact
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ContactRepository: JpaRepository<Contact, String> {
}