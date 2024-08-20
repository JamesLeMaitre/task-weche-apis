package dev.perogroupe.wecheapis.utils.enums

enum class RequestStatus(val value: String) {
    NEW("NEW"),
    PENDING("PENDING"),
    APPROVED("APPROVED"),
    REJECTED("REJECTED"),
    DONE("DONE"),
    UPDATE("UPDATE"), // Waiting for approval
}