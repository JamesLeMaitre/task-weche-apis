package dev.perogroupe.wecheapis.services.impls

import dev.perogroupe.wecheapis.dtos.requests.UpdatePasswordRequest
import dev.perogroupe.wecheapis.dtos.requests.UpdateUserRequest
import dev.perogroupe.wecheapis.dtos.responses.UserResponse
import dev.perogroupe.wecheapis.exceptions.UserNotFoundException
import dev.perogroupe.wecheapis.repositories.UserRepository
import dev.perogroupe.wecheapis.services.UploadService
import dev.perogroupe.wecheapis.services.UserService
import dev.perogroupe.wecheapis.utils.response
import dev.perogroupe.wecheapis.utils.toUpdate
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class UserServiceImpl(
    private val repository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val uploadService: UploadService,
) : UserService {

    /**
     * Updates a user's information.
     *
     * @param request The request containing the updated user information.
     * @param authentication The authentication object containing the user's serial number.
     * @return The updated user response.
     * @throws UserNotFoundException If the user with the provided serial number is not found.
     */
    override fun updateUser(
        request: UpdateUserRequest,
        authentication: Authentication,
    ): UserResponse {
        // Find the user by their serial number
        val user = repository.findByUsername(authentication.name)
            .orElseThrow { UserNotFoundException("User with this serial number ${authentication.name} not found") }

        // Update the user's information
        user.toUpdate(request)

        // Save the updated user and return the response
        return repository.save(user).response()
    }


    /**
     * Updates a user's password.
     *
     * @param request The request containing the updated user password.
     * @param authentication The authentication object containing the user's serial number.
     * @return The updated user response.
     * @throws UserNotFoundException If the user with the provided serial number is not found.
     */
    override fun updateUserPassword(
        request: UpdatePasswordRequest,
        authentication: Authentication,
    ): UserResponse =
        repository.findByUsername(authentication.name)
            .map { user ->
                if (!passwordEncoder.matches(request.oldPassword, user.password)) {
                    throw IllegalArgumentException("Your old password not match for existing password")
                }
                if (!request.newPassword.equals(request.confirmationPassword)) {
                    throw IllegalArgumentException("Your confirmation password not match your new password")
                }
                val upUser = user.copy(password = passwordEncoder.encode(request.newPassword))
                repository.save(upUser).response()
            }.orElseThrow {
                UserNotFoundException("User with serial number ${authentication.name} not found")
            }


    /**
     * Uploads a user's avatar.
     *
     * @param avatar The avatar image file to upload.
     * @param authentication The authentication object containing the user's serial number.
     * @return The user response after uploading the avatar.
     * @throws UserNotFoundException If the user with the provided serial number is not found.
     */
    override fun uploadAvatar(avatar: MultipartFile, authentication: Authentication): UserResponse {
        // Find the user by their serial number
        val user = repository.findByUsername(authentication.name)
            .orElseThrow { UserNotFoundException("User with this serial number ${authentication.name} not found") }

        // Check if the user already has an avatar and delete it
        if (user.avatar != null) {
            uploadService.deleteFile(user.avatar.name)
        }

        // Generate a unique avatar file name
        val avatarFileName = "user_${user.username.replace(" ".toRegex(), "_").lowercase()}_avatar.${avatar.originalFilename?.substringAfterLast(".")}"

        // Upload the new avatar file
        val updatedAvatarPath = "users/avatars/${user.username.replace(" ".toRegex(), "_").lowercase()}/"
        val updatedUser = user.copy(avatar = uploadService.uploadFile(avatar, avatarFileName, updatedAvatarPath))

        // Save the updated user with the new avatar and return the response
        repository.save(updatedUser)
        return updatedUser.response()
    }
}