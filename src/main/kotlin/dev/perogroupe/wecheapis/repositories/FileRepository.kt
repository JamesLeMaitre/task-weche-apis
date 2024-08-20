package dev.perogroupe.wecheapis.repositories

import org.springframework.data.jpa.repository.JpaRepository
import dev.perogroupe.wecheapis.entities.File
import java.util.UUID

interface FileRepository: JpaRepository<File, UUID> {
}