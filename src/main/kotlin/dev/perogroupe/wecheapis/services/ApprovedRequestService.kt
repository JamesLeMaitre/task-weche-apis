package dev.perogroupe.wecheapis.services

import dev.perogroupe.wecheapis.dtos.responses.ApprovedRequestResponse
import org.springframework.data.domain.Page
import org.springframework.security.core.Authentication

interface ApprovedRequestService {

    fun store(id: String): ApprovedRequestResponse

    fun create(id: String): ApprovedRequestResponse

    fun count(authentication: Authentication): Long
}