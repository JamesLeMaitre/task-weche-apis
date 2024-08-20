package dev.perogroupe.wecheapis.configs

import dev.perogroupe.wecheapis.configs.security.JwtAuthenticationConverter
import dev.perogroupe.wecheapis.utils.API_BASE_URL
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler
import org.springframework.security.web.SecurityFilterChain


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class AppSecurityConfig(
    private val converter: JwtAuthenticationConverter
){
    /**
     * Configures the security filter chain for the application.
     *
     * @param http the HttpSecurity object to configure
     * @return the configured SecurityFilterChain
     */
    @Bean
    fun securityWebFilterChain(http: HttpSecurity): SecurityFilterChain {
        // Disable CSRF protection
        http.csrf { csrf -> csrf.disable() }
            // Enable CORS
            .cors { }
            // Configure authorization rules
            .authorizeHttpRequests { authorization ->
                // Allow access to specific endpoints
                authorization.requestMatchers(
                    API_BASE_URL + "login",
                    API_BASE_URL + "register",
                    API_BASE_URL + "user/**",
                    API_BASE_URL + "new-request/user/**",
                    API_BASE_URL + "contact",
                    API_BASE_URL + "hello-retirement",
                    API_BASE_URL + "structure" , "/ws/**","/swagger-ui/**", "/v3/api-docs/**"
                ).permitAll()
                    // Require authentication for all other requests
                    .anyRequest().authenticated()
            }
            // Configure OAuth2 resource server
            .oauth2ResourceServer { oAuth2 ->
                oAuth2.jwt { it.jwtAuthenticationConverter(converter) }
            }
            // Configure exception handling
            .exceptionHandling { exceptionHandling ->
                // Handle access denied exceptions with a custom handler
                exceptionHandling.accessDeniedHandler(BearerTokenAccessDeniedHandler())
                    // Handle authentication exceptions with a custom entry point
                    .authenticationEntryPoint(BearerTokenAuthenticationEntryPoint())
            }
            // Configure session management
            .sessionManagement { session ->
                // Disable session creation
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
        // Build and return the security filter chain
        return http.build()
    }


    /**
     * Defines a Bean that creates an AuthenticationManager based on the provided PasswordEncoder and UserDetailsService.
     * @param passwordEncoder the PasswordEncoder to encode passwords
     * @param userDetailsService the UserDetailsService to retrieve user details
     * @return an AuthenticationManager configured with the specified services
     */
    @Bean
    fun daoAuthenticationConverter(
        passwordEncoder: PasswordEncoder, userDetailsService: UserDetailsService
    ): AuthenticationManager {
        // Create a DaoAuthenticationProvider instance
        val authenticationProvider = DaoAuthenticationProvider()
        // Set the UserDetailsService for the authentication provider
        authenticationProvider.setUserDetailsService(userDetailsService)
        // Set the PasswordEncoder for the authentication provider
        authenticationProvider.setPasswordEncoder(passwordEncoder)
        // Return a ProviderManager with the configured authentication provider
        return ProviderManager(authenticationProvider)
    }
}