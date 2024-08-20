package com.verimsolution.eventapiweb.requests


import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import lombok.Data
import lombok.ToString
import java.io.Serializable

@Data
@ToString
data class LoginRequest(
     @field:NotNull(message = "Username is required") @field:NotBlank(message = "Username is required")
    val username: String? = null,
    @field:NotNull(message = "Password is required") @field:NotBlank(message = "Password is required")
    val password: String? = null
) : Serializable
