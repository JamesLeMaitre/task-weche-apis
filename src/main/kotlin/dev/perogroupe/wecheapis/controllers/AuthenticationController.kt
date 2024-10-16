package dev.perogroupe.wecheapis.controllers

import com.verimsolution.eventapiweb.requests.LoginRequest
import dev.perogroupe.wecheapis.dtos.requests.UpdatePasswordRequest
import dev.perogroupe.wecheapis.dtos.requests.UpdateUserRequest
import dev.perogroupe.wecheapis.dtos.requests.UserAdminRequest
import dev.perogroupe.wecheapis.dtos.requests.UserRequest
import dev.perogroupe.wecheapis.dtos.responses.clients.HttpResponse
import dev.perogroupe.wecheapis.services.AuthenticationService
import dev.perogroupe.wecheapis.services.UploadService
import dev.perogroupe.wecheapis.services.UserService
import dev.perogroupe.wecheapis.utils.API_BASE_URL
import dev.perogroupe.wecheapis.utils.determineContentType
import dev.perogroupe.wecheapis.utils.successResponse
import dev.perogroupe.wecheapis.utils.validationErrorResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus.OK
import org.springframework.http.MediaType
import org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping(value = [API_BASE_URL], produces = [APPLICATION_JSON_VALUE])
class AuthenticationController (
    private val service: AuthenticationService,
    private val userService: UserService,
    private val uploadService: UploadService,
    private val authenticationProvider: AuthenticationManager,
    private val jwtAuthenticationProvider: JwtAuthenticationProvider
) {

    /**
     * Endpoint for user login.
     *
     * @param request The login request object containing the user's credentials.
     * @param result The binding result object containing any validation errors.
     * @return A response entity containing the result of the login operation.
     */
    @PostMapping("login", consumes = [APPLICATION_FORM_URLENCODED_VALUE])
    fun login(@ModelAttribute @Valid request: LoginRequest, result: BindingResult): ResponseEntity<HttpResponse> {
        // Check if there are any validation errors
        if (result.hasErrors()) {
            // If there are errors, return a validation error response
            return validationErrorResponse(result.fieldErrors)
        } else {
            // If there are no errors, attempt to login the user
            val loginResult = service.loginUser(request, authenticationProvider)
            // If the login is successful, return a success response
            return successResponse("User login successfully!", OK, loginResult)
        }
    }


    /**
     * Endpoint for refreshing a user's access token.
     *
     * This endpoint expects a POST request with a form-urlencoded body containing a single parameter "token".
     * The value of the "token" parameter should be the user's current access token.
     *
     * If the token is valid and has not expired, a new access token will be generated and returned in the response body.
     * The new token will have the same expiration time as the original token.
     *
     * @param token The user's current access token.
     * @return A response entity containing the new access token if the refresh was successful, or an error response if the refresh failed.
     */
    @PostMapping("refresh/token", consumes = [APPLICATION_FORM_URLENCODED_VALUE])
    fun refreshToken(@RequestParam("token") token: String): ResponseEntity<HttpResponse> {
        // Call the service to refresh the token
        val refreshResult = service.refreshToken(token, jwtAuthenticationProvider)

        // If the refresh was successful, return a success response with the new token
        return successResponse("Token is updated successfully", OK, refreshResult)
    }

    /**
     * Endpoint for user registration.
     *
     * This endpoint expects a POST request with a form-urlencoded body containing user registration data.
     * The data should be provided in the request body as a [UserRequest] object.
     *
     * If the registration data is valid, a new user will be created and a success response will be returned.
     * The response will contain a message indicating that the registration was successful, and the newly created user object.
     *
     * If the registration data is invalid, an error response will be returned with details of the validation errors.
     *
     * @param request The user registration data.
     * @param result The binding result object containing any validation errors.
     * @return A response entity containing the result of the registration operation.
     */
    @PostMapping("register", consumes = [APPLICATION_FORM_URLENCODED_VALUE])
    fun registerUser(@ModelAttribute @Valid request: UserRequest, result: BindingResult): ResponseEntity<HttpResponse> {
        // Check if there are any validation errors
        return if (result.hasErrors()) {
            // If there are errors, return a validation error response
            validationErrorResponse(result.fieldErrors)
        } else {
            // If there are no errors, attempt to register the user
            val registerResult = service.registerUser(request)
            // If the registration is successful, return a success response
            successResponse("User registration successfully!", OK, registerResult)
        }
    }

    /**
     * This method is used to get the details of the currently logged in user.
     * It retrieves the user's authentication from the SecurityContextHolder and
     * passes it to the service to fetch the user's details.
     *
     * @return A ResponseEntity containing the user's details and a success status.
     */
    @PostMapping("me")
    fun me(): ResponseEntity<HttpResponse> {
        // Get the user's authentication from the SecurityContextHolder
        val authentication = SecurityContextHolder.getContext().authentication

        // Call the service to fetch the user's details
        val userDetails = service.authUser(authentication)

        // Return a success response with the user's details
        return successResponse("Logged in User Details", OK, userDetails)
    }

    /**
     * Updates the user's structure based on the provided ID.
     *
     * @param id The ID of the structure to update.
     * @return A ResponseEntity containing the user's details and a success status.
     */
    @PostMapping("update-structure/{id}")
    fun updateStructure(@PathVariable id: String): ResponseEntity<HttpResponse> {
        // Get the user's authentication from the SecurityContextHolder
        val authentication = SecurityContextHolder.getContext().authentication

        // Call the service to update the user's structure
        val updatedUserDetails = service.updateStructure(id, authentication)

        // Return a success response with the updated user's details
        return successResponse("User Structure Updated", OK, updatedUserDetails)
    }

    /**
     * Updates the user's password.
     *
     * @param request The update password request containing the new password.
     * @param result The binding result containing any validation errors.
     * @return A ResponseEntity containing the result of the update.
     */
    @PostMapping("update-password", consumes = [APPLICATION_FORM_URLENCODED_VALUE])
    fun updatePassword(@ModelAttribute @Valid request: UpdatePasswordRequest, result: BindingResult): ResponseEntity<HttpResponse> =
        if (result.hasErrors()) {
            validationErrorResponse(result.fieldErrors)
        } else successResponse(
            "User password updated successfully!", OK, userService.updateUserPassword(request, SecurityContextHolder.getContext().authentication)
        )

    /**
     * Registers a new admin user.
     *
     * @param request The registration request containing the new admin user information.
     * @param result The binding result containing any validation errors.
     * @return A ResponseEntity containing the result of the registration.
     */
    @PostMapping("admin-register", consumes = [APPLICATION_FORM_URLENCODED_VALUE])
    @PreAuthorize("hasRole('ROLE_S_SUPER_ADMIN')")
    fun adminRegister(@ModelAttribute @Valid request: UserAdminRequest, result: BindingResult): ResponseEntity<HttpResponse> {
        // Check for validation errors
        if (result.hasErrors()) {
            // If there are errors, return a validation error response
            return validationErrorResponse(result.fieldErrors)
        }

        // If there are no errors, register the admin user and return a success response
        return successResponse(
            "Admin registration successfully!", OK, service.registerAdmin(request)
        )
    }

    /**
     * Updates an admin user.
     *
     * @param request The update request containing the new user information.
     * @param result The binding result containing any validation errors.
     * @return A ResponseEntity containing the result of the update.
     */
    @PostMapping("update", consumes = [APPLICATION_FORM_URLENCODED_VALUE])
    @PreAuthorize("hasRole('ROLE_S_SUPER_ADMIN')")
    fun adminUpdate(@ModelAttribute @Valid request: UpdateUserRequest, result: BindingResult): ResponseEntity<HttpResponse> {
        // Check for validation errors
        if (result.hasErrors()) {
            // If there are errors, return a validation error response
            return validationErrorResponse(result.fieldErrors)
        }

        // If there are no errors, update the user and return a success response
        return successResponse(
            "Admin updated successfully!", OK, userService.updateUser(request, SecurityContextHolder.getContext().authentication)
        )
    }

    /**
     * Uploads an avatar file.
     *
     * @param avatar The avatar file to upload.
     * @return A ResponseEntity<HttpResponse> indicating the result of the upload.
     */
    @PostMapping("upload-avatar", consumes = [MULTIPART_FORM_DATA_VALUE])
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    fun upload(@RequestParam avatar: MultipartFile): ResponseEntity<HttpResponse> = successResponse(
        "File upload successfully!", OK, userService.uploadAvatar(avatar, SecurityContextHolder.getContext().authentication)
    )

    /**
     * Downloads a file based on the provided fileName.
     *
     * @param fileName The name of the file to download.
     * @return ResponseEntity<Resource> containing the downloaded file.
     */
    @GetMapping("user/{fileName:.*}")
    fun download(@PathVariable fileName: String): ResponseEntity<Resource> {
        // Load the file based on the fileName
        val resource = uploadService.loadFile(fileName)

        // Determine the content type of the file
        val mediaType: MediaType = determineContentType(fileName)

        // Set the headers with the content type
        val headers = HttpHeaders()
        headers.contentType = mediaType

        // Return the ResponseEntity with the file resource
        return ResponseEntity
            .ok()
            .headers(headers)
            .body(resource)
    }
}