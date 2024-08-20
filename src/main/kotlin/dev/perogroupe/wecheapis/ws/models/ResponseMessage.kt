package dev.perogroupe.wecheapis.ws.models

import lombok.Data

@Data
data class ResponseMessage(
    val text: String,
    val to: String,
)
