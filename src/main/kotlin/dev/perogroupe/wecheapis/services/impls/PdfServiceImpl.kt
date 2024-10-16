package dev.perogroupe.wecheapis.services.impls

import com.itextpdf.io.font.constants.StandardFonts
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.io.source.ByteArrayOutputStream
import com.itextpdf.kernel.colors.DeviceRgb
import com.itextpdf.kernel.font.PdfFont
import com.itextpdf.kernel.font.PdfFontFactory
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.borders.Border
import com.itextpdf.layout.element.*
import com.itextpdf.layout.properties.TabAlignment
import com.itextpdf.layout.properties.TextAlignment
import com.itextpdf.layout.properties.UnitValue
import dev.perogroupe.wecheapis.dtos.requests.CheckValidityRequest
import dev.perogroupe.wecheapis.dtos.requests.DocumentRecordRequest
import dev.perogroupe.wecheapis.events.DnrEvent
import dev.perogroupe.wecheapis.exceptions.UserNotFoundException
import dev.perogroupe.wecheapis.exceptions.ValidityCheckException
import dev.perogroupe.wecheapis.repositories.ApprovedRequestRepository
import dev.perogroupe.wecheapis.repositories.DnrRepository
import dev.perogroupe.wecheapis.repositories.NewRequestRepository
import dev.perogroupe.wecheapis.repositories.UserRepository
import dev.perogroupe.wecheapis.services.CheckValidityService
import dev.perogroupe.wecheapis.services.DocumentRecordService
import dev.perogroupe.wecheapis.services.FileCounterService
import dev.perogroupe.wecheapis.services.PdfService
import dev.perogroupe.wecheapis.utils.enums.FileType
import org.springframework.context.ApplicationEventPublisher
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import java.io.InputStream
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

@Service
class PdfServiceImpl(
    private val approvedRequestRepository: ApprovedRequestRepository,
    private val newRequestRepository: NewRequestRepository,
    private val dnrRepository: DnrRepository,
    private val userRepository: UserRepository,
    private val eventPublisher: ApplicationEventPublisher,
    private val checkValidityService: CheckValidityService,
    private val fileCounterService: FileCounterService,
    private val documentRecordService: DocumentRecordService,
) : PdfService {

    override fun checkWhichFileDelivered(requestNumber: String): ByteArray {
        val request = newRequestRepository.findByRequestNumber(requestNumber).get()
        val attribute = request.beneficiary?.attribute
        println("Beneficiary attribute = $attribute")
        if (attribute == null) {
            throw Exception("Beneficiary attribute is null")
        }
        // use switch case
        return when (request.beneficiary.attribute) {
            "sans-fonction" -> generatePdfCas1(requestNumber, concatNumber(requestNumber, FileType.APP_DOCUMENT))
            "nomme" -> generatePdf(requestNumber , concatNumber(requestNumber, FileType.APP_DOCUMENT))
            else -> generateDocxBasedPdf(requestNumber, concatNumber(requestNumber, FileType.APP_DOCUMENT))
        }

    }

    override fun generatePdf(requestNumber: String, reference: String): ByteArray {
        // Fetch request details
        val request = approvedRequestRepository.findByRequestNumber(requestNumber).get()

        val userSupervisor = request.structure?.let {
            userRepository.findUserByStructureAndRole("ROLE_SUPER_ADMIN", it)
                .orElseThrow { UserNotFoundException("User with this serial number ${it.name} not found") }
        }


        // Prepare the output stream for PDF
        val outputStream = ByteArrayOutputStream()
        val pdfWriter = PdfWriter(outputStream)
        val pdfDocument = PdfDocument(pdfWriter)
        val document = Document(pdfDocument)

        val date = Date()

        // Add header image
        val imagePath = "/images/header.jpeg"  // Path relative to 'resources'
        val imageStream: InputStream? = this::class.java.getResourceAsStream(imagePath)

        if (imageStream != null) {
            val imageData = ImageDataFactory.create(imageStream.readAllBytes())
            val headerImage = Image(imageData).setWidth(545f).setHeight(100f)
            document.add(headerImage)
        } else {
            println("Image not found!")
        }


        // Set up font
//        val font: PdfFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD)

        // Create directionTitle with tab stops
        val directionTitle = Paragraph()
            .add(Text("DIRECTION DE LA PLANIFICATION"))
            .add(Text("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t")) // Add sufficient tabs for spacing
            .add(Text("Cotonou, le ${formatDate(date)}"))
            .setTextAlignment(TextAlignment.LEFT)
            .setFontSize(10f)
//            .setFixedPosition(50f, 750f, 500f) // Adjust position as needed

        document.add(directionTitle)

        // Add new line paragraph
        val subTitle = Paragraph("DE L’ADMINISTRATION ET DES FINANCES\n")
            .setTextAlignment(TextAlignment.LEFT)
            .setMarginTop(1f)
            .setFontSize(10f)
        document.add(subTitle)

        val referenceNumber = Paragraph("N ${reference}/MTPF/DPAF/SGRHTE/ DGSC/SA\n\n")
            .setFontSize(8f)
        document.add(referenceNumber)

        // Add title
        val title = Paragraph("ATTESTATION DE PRESENCE AU POSTE")
            .setFontSize(18f)
            .setBold()
            .setTextAlignment(TextAlignment.CENTER)
            .setMarginTop(20f)
            .setUnderline()
        document.add(title)

        // Add reference
        val referenceParagraph = Paragraph()
            .setMarginTop(20f)
            .add(Text("Référence : ").setBold().setUnderline())
            .add(Text("Décret n° 2024-784 du 07 février 2024 portant nomination au Ministère du Travail et de la Fonction publique."))
            .setTextAlignment(TextAlignment.JUSTIFIED) // Justify the text
        document.add(referenceParagraph)

// First paragraph (employee details)
        val employeeDetails = Paragraph()
            .setMarginTop(20f)
            .add(Text("Le Directeur de la Planification, de l’Administration et des Finances du "))
            .add(Text("Ministère du Travail et de la Fonction publique "))
            .add(Text("soussigné, atteste que monsieur ${request.user?.firstname} ${request.user?.lastname}, "))
            .add(Text("${request.user?.body} "))
            .add(Text("numéro matricule: ${request.serialNumber} de la catégorie A, échelle 1, échelon 9. "))
            .add(Text("nommé ${request.fonction} par décret "))
            .add(Text("rappelé en référence, est resté présent à son poste de travail depuis le "))
            .add(Text("${formatDate(request.user?.ppsDate)} "))
            .add(Text("date de sa prise de fonction, jusqu'à ce jour."))
            .setTextAlignment(TextAlignment.JUSTIFIED)  // Justify the text
            .setFirstLineIndent(50f)  // Add indent to the first line of the paragraph
        document.add(employeeDetails)

// Second paragraph (replacement details)
        val replacementDetails = Paragraph()
            .add(Text("Il a remplacé monsieur ${request.user?.oldUserFirstname} ${request.user?.oldUserLastname}, numéro matricule ${request.user?.oldUserSerialNumber}, de la catégorie A, corps Enseignant, échelle 1, échelon 8.\n"))
            .setFirstLineIndent(50f)  // Apply first line indent to this new paragraph
            .setTextAlignment(TextAlignment.JUSTIFIED)  // Keep the text justified
        document.add(replacementDetails)


        // Add closing statement
        document.add(Paragraph("En foi de quoi, la présente attestation lui est délivrée pour servir et valoir ce que de droit.\n\n\n")
            .setFirstLineIndent(50f)  // Add indent to the first line of the paragraph
        )

        // Signature section
        val signature = Paragraph()
            .setMarginTop(70f)
            .add(Tab())  // Add tab to move text to the right
            .add(Text("Signature\n\n\n"))
            .add(Text("${userSupervisor?.firstname} ${userSupervisor?.lastname}"))
            .setTextAlignment(TextAlignment.RIGHT)  // Align the text to the right side

        document.add(signature)
        // Add footer image
        val footerImagePath = "/images/footer.png"  // Path relative to 'resources'
        val footerImageStream: InputStream? = this::class.java.getResourceAsStream(footerImagePath)

        if (footerImageStream != null) {
            val footerImageData = ImageDataFactory.create(footerImageStream.readAllBytes())
            val footerImage = Image(footerImageData).setWidth(500f).setHeight(10f)
                .setFixedPosition(50f, 10f)  // Set position at the bottom of the page
            document.add(footerImage)
        } else {
            println("Footer image not found!")
        }

        // Close the document
        document.close()

        // Store the document record
        documentRecordService.store(DocumentRecordRequest(requestNumber, "${reference}/MTPF/DPAF/SGRHTE/ DGSC/SA", outputStream.toByteArray()))

        // Return the generated PDF as a byte array
        return outputStream.toByteArray()
    }

    override fun generatePdfCas1(requestNumber: String, reference: String): ByteArray {
        // Fetch request details
        val request = approvedRequestRepository.findByRequestNumber(requestNumber).get()

        val userSupervisor = request.structure?.let {
            userRepository.findUserByStructureAndRole("ROLE_SUPER_ADMIN", it)
                .orElseThrow { UserNotFoundException("User with this serial number ${it.name} not found") }
        }

        // Prepare the output stream for PDF
        val outputStream = ByteArrayOutputStream()
        val pdfWriter = PdfWriter(outputStream)
        val pdfDocument = PdfDocument(pdfWriter)
        val document = Document(pdfDocument)

        val date = Date()

        // Add header image
        val headerImagePath = "/images/header.jpeg"  // Path relative to 'resources'
        val headerImageStream: InputStream? = this::class.java.getResourceAsStream(headerImagePath)

        if (headerImageStream != null) {
            val headerImageData = ImageDataFactory.create(headerImageStream.readAllBytes())
            val headerImage = Image(headerImageData).setWidth(545f).setHeight(100f)
            document.add(headerImage)
        } else {
            println("Header image not found!")
        }

        // Set up font
//        val font: PdfFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD)

        // Create header with date and reference number
        val directionTitle = Paragraph()
            .add(Text("DIRECTION DE LA PLANIFICATION\nDE L’ADMINISTRATION ET DES FINANCES"))
            .add(Text("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"))  // Add sufficient tabs for spacing
            .add(Text("Cotonou, le ${formatDate(date)}"))
            .setTextAlignment(TextAlignment.JUSTIFIED) // Justify the text
            .setFontSize(10f)

        document.add(directionTitle)

        // Add reference numbe

        val referenceNumber = Paragraph("N° ${reference}/MTFP/DPAF/SGRHTE/DGSC/SA")
            .setFontSize(8f)
            .setTextAlignment(TextAlignment.JUSTIFIED) // Justify the text
        document.add(referenceNumber)

        // Add title
        val title = Paragraph("ATTESTATION DE PRESENCE AU POSTE\n\n")
            .setFontSize(18f)
            .setBold()
            .setTextAlignment(TextAlignment.CENTER)
            .setMarginTop(20f)
            .setUnderline()
        document.add(title)

        // Create a table with two columns (label and value)
        val employeeDetailsTable = Table(floatArrayOf(150f, 400f))  // Column widths (adjust as needed)
            .setMarginTop(20f)
            .setWidth(UnitValue.createPercentValue(100f))  // Set table width to 100% of the page

// Add the header information (Centered Text)
        val headerParagraph = Paragraph()
            .add(Text("Le Directeur de la Planification de l’Administration et des Finances du "))
            .add(Text("Ministère du Travail et de la Fonction Publique soussigné atteste que :"))
            .setTextAlignment(TextAlignment.JUSTIFIED)  // Center the header text
            .setFirstLineIndent(50f)  // Add indent at the beginning of the paragraph (adjust the value as needed)
        document.add(headerParagraph)

// Add rows for employee details
        employeeDetailsTable.addCell(Cell().add(Paragraph("Monsieur/Madame, ").setBold()).setBorder(Border.NO_BORDER))
        employeeDetailsTable.addCell(Cell().add(Paragraph(": ${request.user?.firstname} ${request.user?.lastname}")).setBorder(Border.NO_BORDER))

        employeeDetailsTable.addCell(Cell().add(Paragraph("CORPS ").setBold()).setBorder(Border.NO_BORDER))
        employeeDetailsTable.addCell(Cell().add(Paragraph(": ${request.user?.body}")).setBorder(Border.NO_BORDER))

        employeeDetailsTable.addCell(Cell().add(Paragraph("NUMERO MATRICULE ").setBold()).setBorder(Border.NO_BORDER))
        employeeDetailsTable.addCell(Cell().add(Paragraph(": ${request.user?.serialNumber}")).setBorder(Border.NO_BORDER))

        employeeDetailsTable.addCell(Cell().add(Paragraph("DIRECTION ").setBold()).setBorder(Border.NO_BORDER))
        employeeDetailsTable.addCell(Cell().add(Paragraph(": ${request.user?.structure?.name}\n")).setBorder(Border.NO_BORDER))

// Add the final sentence about attendance
        val footerParagraph = Paragraph()
            .add(Text("est effectivement présent à son poste de travail depuis le "))
            .add(Text(formatDate(request.user?.ppsDate)).setBold())
            .add(Text(", date de sa prise de service dans la Fonction Publique, jusqu’à ce jour.\n"))
            .setTextAlignment(TextAlignment.JUSTIFIED)  // Justify the paragraph
            .setFirstLineIndent(50f)


// Add the table to the document
        document.add(employeeDetailsTable)
        document.add(footerParagraph)
        // Add closing statement
        document.add(
            Paragraph("En foi de quoi, la présente attestation lui est délivrée pour servir et valoir ce que de droit.\n\n\n\n\n\n\n")
                .setTextAlignment(TextAlignment.JUSTIFIED)  // Justify the paragraph
                .setFirstLineIndent(50f)  // Add indent at the beginning of the paragraph (adjust the value as needed)
        )

        // Signature section
        val signature = Paragraph()
            .setMarginTop(50f)
            .add(Tab())  // Add tab to move text to the right
            .add(Text("Signature\n\n\n"))
            .add(Text("${userSupervisor?.firstname} ${userSupervisor?.lastname}"))
            .setTextAlignment(TextAlignment.RIGHT)  // Align the text to the right side

        document.add(signature)

        // Add footer image
        val footerImagePath = "/images/footer.png"  // Path relative to 'resources'
        val footerImageStream: InputStream? = this::class.java.getResourceAsStream(footerImagePath)

        if (footerImageStream != null) {
            val footerImageData = ImageDataFactory.create(footerImageStream.readAllBytes())
            val footerImage = Image(footerImageData).setWidth(500f).setHeight(10f)
                .setFixedPosition(50f, 10f)  // Set position at the bottom of the page
            document.add(footerImage)
        } else {
            println("Footer image not found!")
        }

        // Close the document
        document.close()

        // Store the document record
        documentRecordService.store(DocumentRecordRequest(requestNumber, "${reference}/MTFP/DPAF/SGRHTE/DGSC/SA", outputStream.toByteArray()))

        // Return the generated PDF as a byte array
        return outputStream.toByteArray()
    }

    override fun generateDocxBasedPdf(requestNumber: String, reference: String): ByteArray {
        // Fetch request details
        val request = approvedRequestRepository.findByRequestNumber(requestNumber).get()

  /*      val userSupervisor = request.structure?.let {
            userRepository.findUserByStructureAndRole("ROLE_ADMIN", it)
                .orElseThrow { UserNotFoundException("User with this serial number ${it.name} not found") }
        }*/

        // Prepare the output stream for PDF
        val outputStream = ByteArrayOutputStream()
        val pdfWriter = PdfWriter(outputStream)
        val pdfDocument = PdfDocument(pdfWriter)
        val document = Document(pdfDocument)


        // Add header image
        val imagePath = "/images/header.jpeg"  // Path relative to 'resources'
        val imageStream: InputStream? = this::class.java.getResourceAsStream(imagePath)

        if (imageStream != null) {
            val imageData = ImageDataFactory.create(imageStream.readAllBytes())
            val headerImage = Image(imageData).setWidth(545f).setHeight(100f)
            document.add(headerImage)
        } else {
            println("Header image not found!")
        }

        // Add header text (top-left)
        val header = Paragraph()
            .add(Text("DIRECTION DE CABINET\n"))
            .add(Text("SECRETARIAT GENERAL DU MINISTERE\n"))
            .add(Text("DIRECTION DEPARTEMENTALE DU TRAVAIL ET DE LA FONCTION PUBLIQUE DE LA DONGA\n"))
            .add(Text("SERVICE DE L’ADMINISTRATION ET DES FINANCES\n"))
            .setTextAlignment(TextAlignment.CENTER)
            .setFontSize(10f)
            .setBold()
        document.add(header)

        // Create a table with two columns for date and director
        val dateDirectorTable = Table(floatArrayOf(300f, 200f))  // Adjust column widths as needed
            .setWidth(UnitValue.createPercentValue(100f))  // Set table width to 100% of the page

        // Add "Le Directeur Départemental" in the second column, aligned to the right
        dateDirectorTable.addCell(Cell().add(Paragraph("Le Directeur Départemental"))
            .setBorder(Border.NO_BORDER)
            .setTextAlignment(TextAlignment.LEFT))

// Add "Natitingou le {date}" in the first column
        dateDirectorTable.addCell(Cell().add(Paragraph("Natitingou le ${formatDate(Date())}"))
            .setBorder(Border.NO_BORDER)
            .setTextAlignment(TextAlignment.RIGHT))


// Add the table to the document
        document.add(dateDirectorTable)

        // Add document reference number
        val referenceNumber = Paragraph("N° ${reference}/MTFP/DC/SGM/DDTFP-DONGA/SAF/SD")
            .setFontSize(12f)
            .setTextAlignment(TextAlignment.LEFT)
        document.add(referenceNumber)

        // Add title
        val title = Paragraph("ATTESTATION DE PRESENCE AU POSTE")
            .setFontSize(14f)
            .setBold()
            .setTextAlignment(TextAlignment.CENTER)
            .setMarginTop(20f)
            .setUnderline()
        document.add(title)

        // Add the main body text
        val bodyParagraph = Paragraph()
            .add(Text("Je soussigné Monsieur Valéry James AMOUSSOU Directeur Départemental du Travail et de la Fonction Publique de la Donga (DDTFP/Donga), "))
            .add(Text("atteste que Madame ${request.user?.firstname} ${request.user?.lastname}, ${request.user?.profession}, de la catégorie ${request.user?.grade}, échelle 3, échelon 9, "))
            .add(Text("agent contractuel de droit public de l’État affectée à la Direction Départementale du Travail et de la Fonction Publique de la Donga (DDTFP/Donga) par titre de mutation n° 001/MTFP/DC/SGM/DAF/SA du 26 novembre 2020, "))
            .add(Text("y a effectivement pris service le 03 décembre 2020 et est présente à son poste à ce jour.\n"))
            .setTextAlignment(TextAlignment.JUSTIFIED)
            .setMultipliedLeading(1.5f)
            .setFirstLineIndent(30f)
            .setFontSize(12f)
        document.add(bodyParagraph)


        val bodyParagraph2 = Paragraph()
            .add(Text("Elle occupe le poste de Chef du Service de la Fonction Publique et de la Modernisation des Services par intérim cumulativement à ses responsabilités d’Assistante-Régisseur Secondaire."))
            .setTextAlignment(TextAlignment.JUSTIFIED)
            .setMultipliedLeading(1.5f)
            .setFirstLineIndent(30f)
            .setFontSize(12f)
        document.add(bodyParagraph2)

        // Add closing statement
        val closingStatement = Paragraph("En foi de quoi la présente attestation lui est délivrée pour servir et valoir ce que de droit.")
            .setTextAlignment(TextAlignment.JUSTIFIED)
            .setFirstLineIndent(30f)
            .setFontSize(12f)
            .setMarginTop(20f)
        document.add(closingStatement)

        // Add signature section
        val signature = Paragraph()
            .setMarginTop(50f)
            .add(Text("Valéry James AMOUSSOU\n"))
            .add(Text("Directeur Départemental"))
            .setTextAlignment(TextAlignment.RIGHT)
            .setFontSize(10f)
        document.add(signature)

        // Add footer with amplifications
        val footer = Paragraph()
            .add(Text("Ampliations:\n"))
            .add(Text("DPAF ..................... 01\n"))
            .add(Text("Chrono .................. 01\n"))
            .add(Text("Intéressée ............ 01\n"))
            .setFontSize(8f)
            .setTextAlignment(TextAlignment.LEFT)
            .setMarginTop(50f)
        document.add(footer)

        // Add footer image
        val footerImagePath = "/images/footer.png"  // Path relative to 'resources'
        val footerImageStream: InputStream? = this::class.java.getResourceAsStream(footerImagePath)

        if (footerImageStream != null) {
            val footerImageData = ImageDataFactory.create(footerImageStream.readAllBytes())
            val footerImage = Image(footerImageData).setWidth(500f).setHeight(10f)
                .setFixedPosition(50f, 10f)  // Set position at the bottom of the page
            document.add(footerImage)
        } else {
            println("Footer image not found!")
        }
        // Close the document
        document.close()
        // Store the document record
        documentRecordService.store(DocumentRecordRequest(requestNumber, "${reference}/MTFP/DC/SGM/DDTFP-DONGA/SAF/SD", outputStream.toByteArray()))

        // Return the generated PDF as a byte array
        return outputStream.toByteArray()
    }


    override fun generateDnr(authentication: Authentication): ByteArray {
        if (!checkValidityService.checkValidityApp(authentication)) {
            throw ValidityCheckException("User has no validity app document")
        }
        val user = userRepository.findByUsername(authentication.name)
            .orElseThrow { IllegalArgumentException("User not found") }
        val dnr = dnrRepository.findByUser(user)
            // Get the last DNR saved in the list
            .last()
        val request = dnr.newRequest

        // Create Check validity request
        val checkValidityRequest = CheckValidityRequest(
            user = user,
            appDocument = true,
            appDnr = true,
            appDocumentDateDelivery = Instant.now(),
            appDnrDateDelivery = Instant.now()
        )


        // Prepare the output stream for PDF
        val outputStream = ByteArrayOutputStream()
        val pdfWriter = PdfWriter(outputStream)
        val pdfDocument = PdfDocument(pdfWriter)
        val document = Document(pdfDocument)

        val date = Date()

        // Add header image
        val imagePath = "/images/header.jpeg"  // Path relative to 'resources'
        val imageStream: InputStream? = this::class.java.getResourceAsStream(imagePath)

        if (imageStream != null) {
            val imageData = ImageDataFactory.create(imageStream.readAllBytes())
            val headerImage = Image(imageData).setWidth(545f).setHeight(100f)
            document.add(headerImage)
        } else {
            println("Image not found!")
        }

        // Set up font
//        val font: PdfFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD)

        val reference = user.requestNumber?.let { concatNumber(it, FileType.CERTIFICATE_OF_NON_DELISTING) }
        // Create reference number
        val referenceNumber = Paragraph("N° ${user.requestNumber?.let { concatNumber(it, FileType.CERTIFICATE_OF_NON_DELISTING) }} /MTFP/DRSC/UAR-APFP/SD")
            .setFontSize(10f)
            .setTextAlignment(TextAlignment.LEFT)
        document.add(referenceNumber)

        // Add date paragraph
        val dateParagraph = Paragraph("Cotonou, le ${formatDate(date)}")
            .setFontSize(10f)
            .setTextAlignment(TextAlignment.RIGHT)
            .setMarginTop(10f)
        document.add(dateParagraph)

        // Add document title
        val title = Paragraph("CERTIFICAT DE NON RADIATION")
            .setFontSize(16f)
            .setBold()
            .setTextAlignment(TextAlignment.CENTER)
            .setMarginTop(20f)
        document.add(title)

        // Add decorative line
        val decorativeLine = Paragraph("----------*******-----------")
            .setFontSize(12f)
            .setTextAlignment(TextAlignment.CENTER)
            .setMarginTop(10f)
        document.add(decorativeLine)

        // Add employee details
        val employeeDetails = Paragraph()
            .setMarginTop(20f)
            .add(Text("Le Chef du Département de la Réglementation et du Suivi des Carrières certifie que "))
            .add(Text("Monsieur/Madame ${request?.user?.firstname} ${request?.user?.lastname}, "))
            .add(Text("Fonctionnaire de l’État, numéro matricule "))
            .add(Text("${request?.serialNumber}, "))
            .add(Text("corps ${request?.user?.body}, échelle 1, grade ${request?.user?.grade}, en service au Ministère de "))
            .add(Text("${request?.structure?.name}, "))
            .add(Text("né(e) à ${request?.user?.placeOfBirth} le ${formatDate(request?.user?.birthdate)}, "))
            .add(Text("n’est ni candidat(e) au départ volontaire ni radié(e) de la Fonction publique béninoise."))
            .setTextAlignment(TextAlignment.JUSTIFIED)
            .setMultipliedLeading(1.5f)
        document.add(employeeDetails)

        // Add closing statement
        val closingStatement =
            Paragraph("En foi de quoi le présent certificat lui est délivré pour servir et valoir ce que de droit.")
                .setMarginTop(30f)
//            .setFontSize(10f)
                .setTextAlignment(TextAlignment.LEFT)
        document.add(closingStatement)


// Add the signature block, centering it on the right side
        // Create a table with one column for the signature block
        val signatureTable = Table(1)  // Single column table
            .setWidth(UnitValue.createPercentValue(100f))  // Set table width to 100%

// Create the signature paragraph
        val signatureParagraph = Paragraph()
            .setMarginTop(30f)
            .add(Text("Philémon ATCHOU").setBold())  // Add name and set bold
            .add(Text("\nChef du Département de la").setBold())  // First line of title
            .add(Text("\nRéglementation et du Suivi des Carrières").setBold())  // Second line of title
            .setTextAlignment(TextAlignment.RIGHT)  // Set right alignment for the entire paragraph
            .setFontSize(10f)  // Set font size

// Add the signature paragraph to the table cell
        signatureTable.addCell(Cell().add(signatureParagraph)
            .setBorder(Border.NO_BORDER)  // Remove border
            .setTextAlignment(TextAlignment.RIGHT))  // Right-align the cell to the table

// Add the signature table to the document
        document.add(signatureTable)


        // Add footer image
        val footerImagePath = "/images/footer.png"  // Path relative to 'resources'
        val footerImageStream: InputStream? = this::class.java.getResourceAsStream(footerImagePath)

        if (footerImageStream != null) {
            val footerImageData = ImageDataFactory.create(footerImageStream.readAllBytes())
            val footerImage = Image(footerImageData).setWidth(500f).setHeight(10f)
                .setFixedPosition(50f, 10f)  // Set position at the bottom of the page
            document.add(footerImage)
        } else {
            println("Footer image not found!")
        }

        document.close()
        checkValidityService.update(checkValidityRequest)
        eventPublisher.publishEvent(DnrEvent(this))

        val requestNumber = request?.requestNumber
        if(requestNumber !== null && reference !== null) {
            // Store the document record
            documentRecordService.store(DocumentRecordRequest(requestNumber, reference, outputStream.toByteArray()))
        }

        // Return the generated PDF as a byte array
        return outputStream.toByteArray()
    }

    override fun generateValidityPdf(authentication: Authentication): ByteArray {
        if (!checkValidityService.checkValidity(authentication)) {
            throw ValidityCheckException("User has no validity app document & app dnr")
        }
        eventPublisher.publishEvent(DnrEvent(this))
        // Fetch the user from the repository
        val user = userRepository.findByUsername(authentication.name)
            .orElseThrow { IllegalArgumentException("User not found") }

        // Check if the user has a validity valid


        // Get the latest DNR from the repository
        val dnr = dnrRepository.findByUser(user).last()
        val request = dnr.newRequest

        // Prepare the output stream for the PDF
        val outputStream = ByteArrayOutputStream()
        val pdfWriter = PdfWriter(outputStream)
        val pdfDocument = PdfDocument(pdfWriter)
        val document = Document(pdfDocument)

        // Add the current date
        val date = Date()

        // Add header image (if available)
        val imagePath = "/images/header.jpeg"  // Path to the image file
        val imageStream: InputStream? = this::class.java.getResourceAsStream(imagePath)

        if (imageStream != null) {
            val imageData = ImageDataFactory.create(imageStream.readAllBytes())
            val headerImage = Image(imageData).setWidth(545f).setHeight(100f)
            document.add(headerImage)
        } else {
            println("Image not found!")
        }

        // Set up fonts
//        val font: PdfFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD)
        val reference = user.requestNumber?.let { concatNumber(it, FileType.CERTIFICATE_OF_VALIDITY) }

        // Add the reference number
        val referenceNumber = Paragraph("Nº${reference}/MTFP/DCSC/DGFP/DRA/SGDRS/DAAR/Sa")

            .setFontSize(10f)
            .setTextAlignment(TextAlignment.LEFT)
        document.add(referenceNumber)

        // Add the date and location
        val dateParagraph = Paragraph("Cotonou, le ${formatDate(date)}")
            .setFontSize(10f)
            .setTextAlignment(TextAlignment.RIGHT)
            .setMarginTop(10f)
        document.add(dateParagraph)

        // Add the title
        val title = Paragraph("ATTESTATION DE VALIDITE DE SERVICE")
            .setFontSize(16f)
            .setBold()
            .setTextAlignment(TextAlignment.CENTER)
            .setMarginTop(20f)
        document.add(title)

        // Add decorative line
        val decorativeLine = Paragraph("---------------*---------------")
            .setFontSize(12f)
            .setTextAlignment(TextAlignment.CENTER)
            .setMarginTop(10f)
        document.add(decorativeLine)

        // Add employee details
        val employeeDetails = Paragraph()
            .setMarginTop(20f)
            .add(Text("Je soussigné Victorin V. HONVOH; Directeur de Cabinet du Ministre du Travail et de la Fonction Publique "))

            .add(
                Text(
                    "atteste que madame/monsieur ${request?.user?.firstname} ${request?.user?.lastname}, né(e) le ${
                        formatDate(
                            request?.user?.birthdate
                        )
                    }, en/au/à ${request?.user?.placeOfBirth},  "
                )
            )
            .add(Text("est fonctionnaire de l’État/agent contractuel de droit public de l’État en poste au ${request?.structure?.name}. "))
            .add(Text("\n  Les données relatives à sa carrière se déclinent comme suit : "))
            .add(Text("\n - Corps/Emploi : ${request?.user?.body}"))
            .add(Text("\n - Grade : ${request?.user?.grade}"))
            .add(Text("\n - Numéro matricule : ${request?.user?.username}"))
            .add(Text("\n - Date de prise de service : ${formatDate(request?.user?.ppsDate)}"))
            .add(Text("\n - Fonction : ${request?.user?.profession}"))
            .add(
                Text(
                    "\n - Ancienneté au : ${
                        request?.user?.let { calculateDateDifference(Date(), it.ppsDate) }
                            ?.let { absoluteValue(it) }
                    } ans"
                )
            )
//            .add(Text("\n - Ancienneté au : ${request?.seniority}"))
        document.add(employeeDetails)

        // Add statement about departure and retirement
        val closingStatement = Paragraph()
            .setMarginTop(30f)
            .add(Text("L’intéressé(e) n’est ni candidat(e) au Départ Volontaire ni radié(e) de la fonction publique béninoise. "))
            .add(
                Text(
                    "Elle/il fera probablement valoir ses droits à une pension de retraite pour compter du ${formatDate(request?.user?.dateRetreat)} "
                )
            )
            .add(Text("pour limite d’âge des 20 ans"))
//            .setFontSize(10f)
        document.add(closingStatement)

        // Add the final attestation statement
        val finalStatement =
            Paragraph("En foi de quoi la présente attestation lui est délivrée pour servir et valoir ce que de droit.")
                .setFontSize(10f)
                .setMarginTop(30f)
                .setTextAlignment(TextAlignment.LEFT)
        document.add(finalStatement)

        // Add signature block
        val signature =
            Paragraph("Victorin V. HONVOH\n")
                .setFontSize(10f)
                .setTextAlignment(TextAlignment.RIGHT)
                .setMarginTop(50f)
        document.add(signature)

        // Close the document and return the generated PDF as a byte array
        document.close()

        val requestNumber = request?.requestNumber
        if(requestNumber !== null && reference !== null) {
            // Store the document record
            documentRecordService.store(DocumentRecordRequest(requestNumber, "${reference}/MTFP/DCSC/DGFP/DRA/SGDRS/DAAR/Sa", outputStream.toByteArray()))
        }




        return outputStream.toByteArray()


    }


    fun formatDate(date: Date?): String {
        // Check if date is null and return empty string if so
        if (date == null) return ""

        // Convert Date to LocalDate
        val localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()

        // Create DateTimeFormatter with French locale
        val formatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.FRENCH)

        // Format LocalDate
        return localDate.format(formatter)
    }


    /**
     * Write function to calculate date difference between two dates
     * @param startDate
     * @param endDate
     * @return in years
     */
    fun calculateDateDifference(startDate: Date, endDate: Date): Long {
        val startInstant = startDate.toInstant()
        val endInstant = endDate.toInstant()
        val duration = Duration.between(startInstant, endInstant)
        return duration.toDays().div(365)
    }

    fun absoluteValue(value: Long): Long {
        return if (value < 0) -value else value
    }

    // Function who return only current year
    fun getCurrentYear(): Int {
        return getCurrentDate().year
    }

    open fun getCurrentDate(): LocalDate {
        return LocalDate.now()
    }

    fun concatNumber(reference: String,fileType: FileType): String {
        val currentYear = getCurrentYear()
        val count = fileCounterService.increment(fileType, reference)
        return "$currentYear/$count"
    }


}