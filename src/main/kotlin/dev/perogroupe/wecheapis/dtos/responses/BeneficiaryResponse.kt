package dev.perogroupe.wecheapis.dtos.responses

import lombok.Data
import lombok.ToString
import java.time.Instant

@Data
@ToString
data class BeneficiaryResponse(
    val id: String,
    val name: String,
    val attribute: String,
    val createdAt: Instant,
)
