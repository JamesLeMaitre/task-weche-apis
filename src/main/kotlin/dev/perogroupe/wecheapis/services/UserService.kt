package dev.perogroupe.wecheapis.services

import dev.perogroupe.wecheapis.dtos.requests.UpdatePasswordRequest
import dev.perogroupe.wecheapis.dtos.requests.UpdateUserRequest
import dev.perogroupe.wecheapis.dtos.responses.UserResponse
import org.springframework.security.core.Authentication
import org.springframework.web.multipart.MultipartFile

interface UserService {
    fun updateUser(request: UpdateUserRequest, authentication: Authentication): UserResponse
    fun updateUserPassword(request: UpdatePasswordRequest, authentication: Authentication): UserResponse
    fun uploadAvatar(avatar: MultipartFile, authentication: Authentication): UserResponse
}