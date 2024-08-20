package dev.perogroupe.wecheapis.dtos.responses.clients
import lombok.Data
import lombok.ToString
import java.io.Serializable

@Data
@ToString
data class JwtResponse(
    val accessToken: String,
    val refreshToken: String
) : Serializable
