package dev.perogroupe.wecheapis.services

import dev.perogroupe.wecheapis.dtos.requests.CheckValidityRequest
import dev.perogroupe.wecheapis.entities.CheckValidity
import org.springframework.security.core.Authentication

interface CheckValidityService {
    fun store(checkValidityRequest: CheckValidityRequest): CheckValidity?
    fun update(request: CheckValidityRequest): CheckValidity
    fun checkValidityApp(authentication: Authentication): Boolean
    fun checkValidity(authentication: Authentication): Boolean
}