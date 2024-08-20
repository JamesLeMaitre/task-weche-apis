package dev.perogroupe.wecheapis.services

interface PdfService {
    fun generatePdf(requestNumber: String): ByteArray
}