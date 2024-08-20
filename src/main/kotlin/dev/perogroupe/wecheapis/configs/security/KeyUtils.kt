package dev.perogroupe.wecheapis.configs.security


import org.springframework.beans.factory.annotation.Value
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component
import java.io.File
import java.io.FileOutputStream
import java.nio.file.Files
import java.security.KeyFactory
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.EncodedKeySpec
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.*


@Component
class KeyUtils(
    var environment: Environment,
    @Value("\${access-token.private}")
    private val accessTokenPrivateKeyPath: String,
    @Value("\${access-token.public}")
    private val accessTokenPublicKeyPath: String,
    @Value("\${refresh-token.private}")
    private val refreshTokenPrivateKeyPath: String,
    @Value("\${refresh-token.public}")
    private val refreshTokenPublicKeyPath: String,
) {

    private var _accessTokenKeyPair: KeyPair? = null
    private var _refreshTokenKeyPair: KeyPair? = null

    private val accessTokenKeyPair: KeyPair
        get() {
            if (Objects.isNull(_accessTokenKeyPair)) {
                _accessTokenKeyPair = getKeyPair(accessTokenPublicKeyPath, accessTokenPrivateKeyPath)
            }
            return _accessTokenKeyPair!!
        }

    private val refreshTokenKeyPair: KeyPair
        get() {
            if (Objects.isNull(_refreshTokenKeyPair)) {
                _refreshTokenKeyPair = getKeyPair(refreshTokenPublicKeyPath, refreshTokenPrivateKeyPath)
            }
            return _refreshTokenKeyPair!!
        }
    /**
     * Retrieves or generates a new KeyPair based on the provided public and private key paths.
     *
     * @param publicKeyPath The path to the public key file.
     * @param privateKeyPath The path to the private key file.
     * @return The KeyPair generated from the keys.
     * @throws RuntimeException if public and private keys do not exist in production environment.
     */
    private fun getKeyPair(publicKeyPath: String, privateKeyPath: String): KeyPair {
        var keyPair: KeyPair? = null

        // Check if the public and private key files exist
        val publicKeyFile = File(publicKeyPath)
        val privateKeyFile = File(privateKeyPath)
        if (privateKeyFile.exists() && publicKeyFile.exists()) {
            try {
                // Retrieve public key
                val keyFactory = KeyFactory.getInstance("RSA")
                val publicKeyBytes = Files.readAllBytes(publicKeyFile.toPath())
                val publicKeySpec: EncodedKeySpec = X509EncodedKeySpec(publicKeyBytes)
                val publicKey = keyFactory.generatePublic(publicKeySpec)

                // Retrieve private key
                val privateKeyBytes = Files.readAllBytes(privateKeyFile.toPath())
                val privateKeySpec = PKCS8EncodedKeySpec(privateKeyBytes)
                val privateKey = keyFactory.generatePrivate(privateKeySpec)

                // Create and return KeyPair
                keyPair = KeyPair(publicKey, privateKey)
                return keyPair
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            // Throw exception if keys do not exist in production environment
            if (listOf(*environment.activeProfiles).contains("prod")) {
                throw RuntimeException("Public and Private key does not exist")
            }
        }

        // Create directory if it doesn't exist
        val directory = File("access-refresh-token-keys")
        if (!directory.exists()) {
            directory.mkdirs()
        }

        try {
            // Generate new KeyPair
            val keyPairGenerator = KeyPairGenerator.getInstance("RSA")
            keyPairGenerator.initialize(2048)
            keyPair = keyPairGenerator.generateKeyPair()

            // Write public key to file
            FileOutputStream(publicKeyPath).use { fos ->
                val keySpec = X509EncodedKeySpec(keyPair.public.encoded)
                fos.write(keySpec.encoded)
            }

            // Write private key to file
            FileOutputStream(privateKeyPath).use { fos ->
                val keySpec = PKCS8EncodedKeySpec(keyPair!!.private.encoded)
                fos.write(keySpec.encoded)
            }
        } catch (e: Exception) {
            e.printStackTrace();
        }

        return keyPair!!
    }

    val accessTokenPublicKey: RSAPublicKey
        get() = accessTokenKeyPair.public as RSAPublicKey
    val accessTokenPrivateKey: RSAPrivateKey
        get() = accessTokenKeyPair.private as RSAPrivateKey
    val refreshTokenPublicKey: RSAPublicKey
        get() = refreshTokenKeyPair.public as RSAPublicKey
    val refreshTokenPrivateKey: RSAPrivateKey
        get() = refreshTokenKeyPair.private as RSAPrivateKey
}