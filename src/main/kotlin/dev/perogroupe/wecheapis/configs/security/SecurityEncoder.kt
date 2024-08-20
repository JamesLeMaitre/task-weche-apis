package dev.perogroupe.wecheapis.configs.security


import com.nimbusds.jose.jwk.JWK
import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jose.jwk.source.ImmutableJWKSet
import com.nimbusds.jose.jwk.source.JWKSource
import com.nimbusds.jose.proc.SecurityContext
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider

@Configuration
class SecurityEncoder (
    private val keyUtils: KeyUtils,
    private val converter: JwtAuthenticationConverter
){

    /**
     * Bean for creating a JwtDecoder for decoding JWT access tokens.
     * The decoder is configured with the public key used to sign the tokens.
     *
     * @return a JwtDecoder instance
     */
    @Bean
    @Primary
    fun jwtAccessTokenDecoder(): JwtDecoder {
        // Create a JwtDecoder using the public key to verify the token's signature
        return NimbusJwtDecoder.withPublicKey(keyUtils.accessTokenPublicKey).build()
    }

    /**
     * Bean for creating a JwtEncoder for encoding JWT access tokens.
     * The encoder is configured with the public key used to sign the tokens.
     *
     * @return a JwtEncoder instance
     */
    @Bean
    @Primary
    fun jwtAccessTokenEncoder(): JwtEncoder {
        // Create a JWK (JSON Web Key) using the public key to sign the token
        val jwk: JWK = RSAKey.Builder(keyUtils.accessTokenPublicKey)
            .privateKey(keyUtils.accessTokenPrivateKey) // Set the private key for signing
            .build()

        // Create a JWKSource from the JWK set containing the public key
        val jwkSource: JWKSource<SecurityContext> = ImmutableJWKSet(JWKSet(jwk))

        // Create a NimbusJwtEncoder using the JWKSource for signing the token
        return NimbusJwtEncoder(jwkSource)
    }

    /**
     * Bean for creating a JwtDecoder for decoding JWT refresh tokens.
     * The decoder is configured with the public key used to sign the tokens.
     *
     * @return a JwtDecoder instance
     */
    @Bean
    @Qualifier("jwtRefreshTokenDecoder")
    fun jwtRefreshTokenDecoder(): JwtDecoder {
        return NimbusJwtDecoder.withPublicKey(keyUtils.refreshTokenPublicKey).build()
    }

    /**
     * Bean for creating a JwtEncoder for encoding JWT refresh tokens.
     * The encoder is configured with the public key used to sign the tokens.
     *
     * @return a JwtEncoder instance
     */
    @Bean
    @Qualifier("jwtRefreshTokenEncoder")
    fun jwtRefreshTokenEncoder(): JwtEncoder {
        // Create a JWK (JSON Web Key) using the public key to sign the token
        val jwk: JWK = RSAKey.Builder(keyUtils.refreshTokenPublicKey)
            .privateKey(keyUtils.refreshTokenPrivateKey)
            .build()
        // Create a JWKSource from the JWK set containing the public key
        val jwkSource: JWKSource<SecurityContext> = ImmutableJWKSet(JWKSet(jwk))
        // Create a NimbusJwtEncoder using the JWKSource for signing the token
        return NimbusJwtEncoder(jwkSource)
    }

    /**
     * This function creates a JwtAuthenticationProvider.
     * It uses the jwtRefreshTokenDecoder() and sets the jwtAuthenticationConverter.
     *
     * @return JwtAuthenticationProvider
     */
    @Bean
    @Qualifier("JwtRefreshAuthenticationProvider")
    fun jwtAuthenticationProvider(): JwtAuthenticationProvider {
        // Create a JwtAuthenticationProvider
        val provider = JwtAuthenticationProvider(jwtRefreshTokenDecoder())

        // Set the JwtAuthenticationConverter
        provider.setJwtAuthenticationConverter(converter)

        // Return the JwtAuthenticationProvider
        return provider
    }

}