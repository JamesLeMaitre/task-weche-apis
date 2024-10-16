package dev.perogroupe.wecheapis.dtos.requests

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import lombok.Data
import lombok.ToString


@Data
@ToString
data class BeneficiaryRequest(
    @field:NotBlank(message = "Beneficiary name cannot be blank")
    @field:NotNull(message = "Beneficiary name cannot be null")
    val name: String,
    @field:NotBlank(message = "Beneficiary attribute cannot be blank")
    @field:NotNull(message = "Beneficiary attribute cannot be null")
    val attribute: String
)
