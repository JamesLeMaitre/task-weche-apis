package dev.perogroupe.wecheapis.exceptions

class BeneficiaryNotFoundException (message: String): Exception(message) {
    constructor(): this("Beneficiary not found")
}