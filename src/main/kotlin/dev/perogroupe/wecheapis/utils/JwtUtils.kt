package dev.perogroupe.wecheapis.utils

import dev.perogroupe.wecheapis.dtos.responses.clients.JwtResponse
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.oauth2.core.OAuth2Error
import org.springframework.security.oauth2.core.OAuth2TokenValidator
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.JwtClaimsSet
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.JwtEncoderParameters
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.Instant
import java.time.temporal.ChronoUnit

@Component
class JwtUtils(
    private val accessTokenEncoder: JwtEncoder,
    @Qualifier("jwtRefreshTokenEncoder")
    private val refreshTokenEncoder: JwtEncoder
) {

    /**
     * Creates an access token based on the provided authentication.
     *
     * @param authentication the authentication object
     * @return the generated access token
     */
    private fun createAccessToken(authentication: Authentication): String {
        // Extract user details from authentication
        val user = authentication.principal as UserDetails

        // Get the current time
        val now = Instant.now()

        // Get user claims
        val claims = getClaimsFromUser(user)

        // Build JwtClaimsSet with appropriate claims
        val claimsSet = JwtClaimsSet.builder()
            .issuer("keyApp")
            .issuedAt(now)
            .expiresAt(now.plus(5, ChronoUnit.DAYS))
            .subject(user.username)
            .claim(AUTHORITIES, claims)
            .build()

        // Encode JwtClaimsSet to generate access token
        return accessTokenEncoder.encode(JwtEncoderParameters.from(claimsSet)).tokenValue
    }


    /**
     * Creates a refresh token based on the provided authentication.
     *
     * @param authentication the authentication object
     * @return the generated refresh token
     */
    private fun createRefreshToken(authentication: Authentication): String {
        // Extract user details from authentication
        val user = authentication.principal as UserDetails

        // Get the current time
        val now = Instant.now()

        // Get user claims
        val claims = getClaimsFromUser(user)

        // Build JwtClaimsSet with appropriate claims
        val claimsSet = JwtClaimsSet.builder()
            .issuer("keyApp")
            .issuedAt(now)
            .expiresAt(now.plus(1, ChronoUnit.DAYS))
            .subject(user.username)
            .claim(AUTHORITIES, claims)
            .build()

        // Encode JwtClaimsSet to generate refresh token
        return refreshTokenEncoder.encode(JwtEncoderParameters.from(claimsSet)).tokenValue
    }

    /**
     * Retrieves the claims from the provided user details.
     *
     * @param user the user details object
     * @return an array of strings containing user claims
     */
    private fun getClaimsFromUser(user: UserDetails): Array<String> = user.authorities
        .map { obj: GrantedAuthority -> obj.authority }
        .toTypedArray()

    fun getJwtToken(authentication: Authentication): JwtResponse {
        if (authentication.principal !is UserDetails) {
            throw BadCredentialsException("Serial number or password is incorrect")
        }
        val refreshToken: String = if (authentication.credentials is Jwt) {
            val now = Instant.now()
            val expireAt = (authentication.credentials as Jwt).expiresAt!!
            val duration = Duration.between(now, expireAt)
            val dayUntilExpired = duration.toDays()
            if (dayUntilExpired < 7) {
                createRefreshToken(authentication)
            } else {
                (authentication.credentials as Jwt).tokenValue
            }
        } else {
            createRefreshToken(authentication)
        }
        return JwtResponse(
            accessToken = createAccessToken(authentication),
            refreshToken = refreshToken
        )
    }

    fun validateToken(jwt: String): Boolean = try {
        audienceValidator().validate(Jwt.withTokenValue(jwt).build())
        true
    } catch (e: Exception) {
        false
    }


    fun audienceValidator(): OAuth2TokenValidator<Jwt> {
        return AudienceValidator()
    }

    internal class AudienceValidator : OAuth2TokenValidator<Jwt> {
        private var error: OAuth2Error = OAuth2Error("custom_code", "Custom error message", null)

        override fun validate(jwt: Jwt): OAuth2TokenValidatorResult {
            return if (jwt.audience.contains("messaging")) {
                OAuth2TokenValidatorResult.success()
            } else {
                OAuth2TokenValidatorResult.failure(error)
            }
        }
    }
}