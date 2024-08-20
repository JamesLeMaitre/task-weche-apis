package dev.perogroupe.wecheapis.services.impls

import com.verimsolution.eventapiweb.requests.LoginRequest
import dev.perogroupe.wecheapis.dtos.requests.UpdatePasswordRequest
import dev.perogroupe.wecheapis.dtos.requests.UpdateUserRequest
import dev.perogroupe.wecheapis.dtos.requests.UserAdminRequest
import dev.perogroupe.wecheapis.dtos.requests.UserRequest
import dev.perogroupe.wecheapis.dtos.responses.UserResponse
import dev.perogroupe.wecheapis.dtos.responses.clients.JwtResponse
import dev.perogroupe.wecheapis.entities.User
import dev.perogroupe.wecheapis.exceptions.StructureNotFoundException
import dev.perogroupe.wecheapis.exceptions.UserNotFoundException
import dev.perogroupe.wecheapis.repositories.RoleRepository
import dev.perogroupe.wecheapis.repositories.StructureRepository
import dev.perogroupe.wecheapis.repositories.UserRepository
import dev.perogroupe.wecheapis.services.AuthenticationService
import dev.perogroupe.wecheapis.services.UploadService
import dev.perogroupe.wecheapis.utils.AppConverter
import dev.perogroupe.wecheapis.utils.JwtUtils
import dev.perogroupe.wecheapis.utils.UserPrincipal
import dev.perogroupe.wecheapis.utils.response
import dev.perogroupe.wecheapis.utils.toDate
import dev.perogroupe.wecheapis.utils.toUpdate
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.lang.IllegalArgumentException
import javax.management.relation.RoleNotFoundException

@Service
class AuthenticationServiceImpl(
    private val utils: JwtUtils,
    private val converter: AppConverter,
    private val roleRepository: RoleRepository,
    private val structureRepository: StructureRepository,
    private val repository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
) : AuthenticationService {


    /**
     * Registers a new user.
     *
     * @param request The user registration request.
     * @return The registered user.
     * @throws IllegalArgumentException If the password and confirmation password are not the same.
     * @throws IllegalArgumentException If the email already exists.
     */
    override fun registerUser(request: UserRequest): UserResponse {
        // Check if the password and confirmation password are the same
        if (request.password != request.confirmPassword) {
            throw IllegalArgumentException("Password and confirmation password are not the same")
        }

        // Check if the email already exists
        if (repository.existsUserByEmail(request.email!!)) {
            throw IllegalArgumentException("Email already exists.")
        }

        // Convert the user request to a user object and set the password and isNotLocked flag
        val user = converter.userRequestToUser(request).copy(
            password = passwordEncoder.encode(request.password),
            isNotLocked = true
        )

        // Set the default role for the user
        setRoles(user, "ROLE_SUPER_ADMIN")

        // Save the user and return the response
        return repository.save(user).response()
    }



    /**
     * Refreshes a JWT token using the provided JwtAuthenticationProvider.
     *
     * @param token The JWT token to refresh.
     * @param jwtAuthProvider The JwtAuthenticationProvider used to authenticate the token.
     * @return A new JwtResponse containing the refreshed token.
     * @throws IllegalArgumentException If the provided token is invalid or expired.
     */
    override fun refreshToken(token: String, jwtAuthProvider: JwtAuthenticationProvider): JwtResponse {
        // Authenticate the provided token using the JwtAuthenticationProvider
        val authentication = jwtAuthProvider.authenticate(BearerTokenAuthenticationToken(token))

        // Generate a new JwtResponse containing the refreshed token
        return utils.getJwtToken(authentication)
    }

    /**
     * Registers a new admin user.
     *
     * @param request The admin registration request.
     * @return The registered admin user.
     * @throws IllegalArgumentException If the password and confirmation password are not the same.
     * @throws StructureNotFoundException If the structure with the provided id is not found.
     */
    /*override fun registerAdmin(request: UserAdminRequest): UserResponse {
        // Check if the password and confirmation password are the same
        if (request.password != request.confirmPassword) {
            throw IllegalArgumentException("Password and confirmation password are not the same")
        }

        // Find the structure by id or throw an exception if not found
        val structure = structureRepository.findById(request.structureId.toString())
            .orElseThrow { StructureNotFoundException("Structure with this id ${request.structureId} not found") }

        // Create a new user based on the request and set default values
        val user = converter.userRequestToUser(request).copy(
            password = passwordEncoder.encode(request.password),
            isNotLocked = true,
            structure = structure
        )

        // Set roles for the user
        setRoles(user, request.roleName!!)

        // Save the user and return the response
        return repository.save(user).response()
    }*/



    /**
     * Registers a new admin user.
     *
     * @param request The admin registration request.
     * @return The registered admin user.
     * @throws IllegalArgumentException If the password and confirmation password are not the same.
     * @throws StructureNotFoundException If the structure with the provided id is not found.
     */
    override fun registerAdmin(request: UserAdminRequest): UserResponse {
        if (request.password != request.confirmPassword) {
            throw IllegalArgumentException("Password and confirmation password are not the same")
        }

        val structure = structureRepository.findByIdOrThrow(request.structureId.toString())

        val user = converter.userRequestToUser(request).apply {
            password = passwordEncoder.encode(request.password)
            isNotLocked = true
            this.structure = structure
        }

        setRoles(user, request.roleName!!)

        return repository.save(user).response()
    }

    /**
     * Finds an entity by ID or throws a StructureNotFoundException if not found.
     *
     * @param id The ID of the entity to find.
     * @return The entity if found.
     * @throws StructureNotFoundException If the entity with the provided ID is not found.
     */
    fun <T> JpaRepository<T, String>.findByIdOrThrow(id: String): T =
        findById(id).orElseThrow { StructureNotFoundException("Structure with this id $id not found") }


    /**
     * Authenticates a user based on the provided authentication.
     *
     * @param authentication The authentication object containing the user's serial number.
     * @return The user's response.
     * @throws UserNotFoundException If the user with the provided serial number is not found.
     */
    override fun authUser(authentication: Authentication): UserResponse {
        // Find the user by their serial number
        return repository.findByUsername(authentication.name)
            .map { user ->
                // Map the user to their response
                user.response()
            }
            .orElseThrow {
                // Throw an exception if the user is not found
                UserNotFoundException("User with this serial number ${authentication.name} not found")
            }
    }

    /**
     * Logs in a user using the provided login request and authentication provider.
     *
     * @param request The login request containing the username and password.
     * @param authenticationProvider The authentication provider used to authenticate the user.
     * @return A JwtResponse containing the JWT token for the authenticated user.
     */
    override fun loginUser(
        request: LoginRequest,
        authenticationProvider: AuthenticationManager,
    ): JwtResponse {
        // Create an authentication request using the provided username and password
        val authenticationRequest =
            UsernamePasswordAuthenticationToken.unauthenticated(request.username, request.password)

        // Authenticate the user using the authentication provider
        val authenticationResponse = authenticationProvider.authenticate(authenticationRequest)

        // Generate a JWT token for the authenticated user
        return utils.getJwtToken(authenticationResponse)
    }

    override fun updateUser(
        id: String,
        request: UpdateUserRequest,
        authentication: Authentication,
    ): UserResponse {
        TODO("Not yet implemented")
    }



    /**
     * Updates the structure for a user.
     *
     * @param id The ID of the structure to update.
     * @param authentication The authentication details of the user.
     * @return The updated user response.
     * @throws UserNotFoundException If the user with the provided serial number is not found.
     * @throws StructureNotFoundException If the structure with the provided ID is not found.
     */
    override fun updateStructure(
        id: String,
        authentication: Authentication,
    ): UserResponse = repository.findByUsername(authentication.name)
        // Find the user by their serial number
        .map { user ->
            // Find the structure by its ID
            val structure = structureRepository.findById(id)
                .orElseThrow { StructureNotFoundException("Structure with this id $id not found") }
            // Update the user's structure
            user.structure = structure
            // Save the updated user and return the response
            repository.save(user).response()
        }
        // Throw an exception if the user is not found
        .orElseThrow { UserNotFoundException("User with this serial number ${authentication.name} not found") }




    /**
     * Sets the roles of a user.
     *
     * @param user The user whose roles are to be set.
     * @param roleName The name of the role to be set.
     * @throws RoleNotFoundException If the role with the given name is not found in the database.
     */
    private fun setRoles(user: User, roleName: String) {
        // Clear the user's existing roles
        user.roles.clear()

        // Find the role with the given name in the database
        val role = roleRepository.findByRoleName(roleName)
            .orElseThrow { RoleNotFoundException("Role $roleName not found in database, please create it") }

        // Add the role to the user's roles
        user.roles.add(role)
    }

    /**
     * Loads a user by username.
     *
     * @param username The username of the user to load.
     * @return The UserDetails of the loaded user.
     * @throws UserNotFoundException If the user with the provided username is not found.
     */
    override fun loadUserByUsername(username: String): UserDetails {
        return repository.findByUsername(username)
            .map { UserPrincipal(it) }
            .orElseThrow { UserNotFoundException("User with this serial number $username not found") }
    }

}