package dev.perogroupe.wecheapis.services

import com.verimsolution.eventapiweb.requests.LoginRequest
import dev.perogroupe.wecheapis.dtos.requests.UpdatePasswordRequest
import dev.perogroupe.wecheapis.dtos.requests.UpdateUserRequest
import dev.perogroupe.wecheapis.dtos.requests.UserAdminRequest
import dev.perogroupe.wecheapis.dtos.requests.UserRequest
import dev.perogroupe.wecheapis.dtos.responses.UserResponse
import dev.perogroupe.wecheapis.dtos.responses.clients.JwtResponse
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider
import org.springframework.web.multipart.MultipartFile

interface AuthenticationService : UserDetailsService {
    fun registerUser(request: UserRequest): UserResponse
    fun refreshToken(token: String, jwtAuthProvider: JwtAuthenticationProvider): JwtResponse
    fun registerAdmin(request: UserAdminRequest): UserResponse
    fun authUser(authentication: Authentication): UserResponse
    fun loginUser(request: LoginRequest, authenticationProvider: AuthenticationManager): JwtResponse

    fun updateUser(id:String, request: UpdateUserRequest, authentication: Authentication): UserResponse
    fun updateStructure(id:String,authentication: Authentication): UserResponse

}