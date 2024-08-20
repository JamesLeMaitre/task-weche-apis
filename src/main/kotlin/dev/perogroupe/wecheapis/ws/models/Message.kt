package dev.perogroupe.wecheapis.ws.models

import lombok.Data

@Data
data class Message(
    val text: String,
    val to: String,
)
