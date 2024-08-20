package dev.perogroupe.wecheapis.dtos.requests
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import lombok.Data
import lombok.ToString
import java.io.Serializable

@Data
@ToString
data class ApprovedRequestReq(
    @field:NotNull @field:NotBlank
    val requestId: String
):Serializable
