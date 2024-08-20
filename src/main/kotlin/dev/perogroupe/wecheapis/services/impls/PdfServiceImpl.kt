package dev.perogroupe.wecheapis.services.impls

import com.itextpdf.io.source.ByteArrayOutputStream
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import dev.perogroupe.wecheapis.repositories.ApprovedRequestRepository
import dev.perogroupe.wecheapis.services.PdfService
import org.springframework.stereotype.Service

@Service
class PdfServiceImpl(
    private val approvedRequestRepository: ApprovedRequestRepository,
) : PdfService {

    override fun generatePdf(requestNumber: String): ByteArray {
        val request = approvedRequestRepository.findByRequestNumber(requestNumber).get()

        val outputStream = ByteArrayOutputStream()
        PdfDocument(PdfWriter(outputStream)).use { pdfDocument ->
            val document = Document(pdfDocument)

            // Ajouter le titre
            document.add(Paragraph("Attestation de présence au poste").setFontSize(18f))

            // Ajouter les informations de la demande
//            document.add(Paragraph("ID: ${request.id}"))
            document.add(Paragraph("Nom: ${request.firstName}"))
            document.add(Paragraph("Prénom: ${request.lastName}"))
            document.add(Paragraph("Date Profession: ${request.dateOfFirstEntryService}"))
        }

        return outputStream.toByteArray()
    }
}