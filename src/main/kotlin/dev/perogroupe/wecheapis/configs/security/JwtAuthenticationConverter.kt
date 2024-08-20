package dev.perogroupe.wecheapis.configs.security


import dev.perogroupe.wecheapis.exceptions.UserNotFoundException
import dev.perogroupe.wecheapis.repositories.UserRepository
import dev.perogroupe.wecheapis.utils.UserPrincipal
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.oauth2.jwt.Jwt

@Configuration
class JwtAuthenticationConverter(
    private val repository: UserRepository
) : Converter<Jwt, UsernamePasswordAuthenticationToken> {
    /**
     * This function converts a JWT token into a UsernamePasswordAuthenticationToken.
     * It first tries to find a user with the subject of the JWT in the repository.
     * If a user is found, it creates a UserPrincipal object with the user and creates
     * a UsernamePasswordAuthenticationToken object with the UserPrincipal, the JWT,
     * and the user's authorities. If no user is found, it throws a UserNotFoundException.
     *
     * @param source The JWT token to convert.
     * @return The converted UsernamePasswordAuthenticationToken.
     * @throws UserNotFoundException If no user is found with the subject of the JWT.
     */
    override fun convert(source: Jwt): UsernamePasswordAuthenticationToken =
        // Try to find a user with the subject of the JWT
        repository.findByUsername(source.subject).map { UserPrincipal(it) }
            // If a user is found, create a UsernamePasswordAuthenticationToken with the user, the JWT, and the user's authorities
            .map { UsernamePasswordAuthenticationToken(it, source, it.authorities) }
            // If no user is found, throw a UserNotFoundException
            .orElseThrow { UserNotFoundException("User with ${source.subject} not found") }
}