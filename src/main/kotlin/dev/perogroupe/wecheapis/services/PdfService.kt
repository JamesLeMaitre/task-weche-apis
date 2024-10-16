package dev.perogroupe.wecheapis.services

import org.springframework.security.core.Authentication

interface PdfService {
    fun generatePdf(requestNumber: String, reference: String): ByteArray

    fun generatePdfCas1(requestNumber: String, reference: String): ByteArray

    fun checkWhichFileDelivered(requestNumber: String): ByteArray

    fun generateDocxBasedPdf(requestNumber: String, reference: String): ByteArray

    fun generateDnr(authentication: Authentication): ByteArray

    fun generateValidityPdf(authentication: Authentication): ByteArray
}