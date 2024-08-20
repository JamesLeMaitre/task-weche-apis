package dev.perogroupe.wecheapis.utils

import dev.perogroupe.wecheapis.dtos.responses.clients.ErrorFieldResponse
import dev.perogroupe.wecheapis.dtos.responses.clients.HttpResponse
import dev.perogroupe.wecheapis.dtos.responses.clients.HttpResponse.Error
import dev.perogroupe.wecheapis.dtos.responses.clients.HttpResponse.Success
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatusCode
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.multipart.MultipartFile
import java.security.SecureRandom
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

const val API_BASE_URL = "api/v1/"
const val AUTHORITIES = "Authorities"
const val ALPHA_NUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWYZabcdefghijklmnopqrstuvwxyz0123456789"
const val ALPHA_NUMERIC_PASSWORD =
    "ABCDEFGHIJKLMNOPQRSTUVWYZabcdefghijklmnopqrstuvwxyz0123456789/*-+.&é'§èçà_|@#^è!ç,;:=?$%"
const val NUMERIC = "0123456789"
const val ADMIN_EMAIL = "assistance.library.platform@gmail.com"
private val RANDOM: Random = SecureRandom()

/**
 * Returns a ResponseEntity containing a Success HttpResponse with the given message, status, and data.
 *
 * @param message The success message.
 * @param status The HTTP status code for the response.
 * @param data The data to be included in the response.
 * @return A ResponseEntity containing a Success HttpResponse.
 */
fun <T : Any> successResponse(
    message: String,
    status: HttpStatusCode,
    data: T
): ResponseEntity<HttpResponse> {
    // Create a Success HttpResponse with the given message, status, and data
    val response = Success(status, message, data)

    // Return a ResponseEntity with the given status and Success HttpResponse
    return ResponseEntity.status(status).body(response)
}

/**
 * Returns a ResponseEntity containing an Error HttpResponse with the given message and status.
 *
 * @param status The HTTP status code for the response.
 * @param message The error message.
 * @return A ResponseEntity containing an Error HttpResponse.
 */
fun errorResponse(status: HttpStatus, message: String): ResponseEntity<Error> {
    return ResponseEntity.status(status).body(Error(status, message, status.reasonPhrase, null))
}

/**
 * Returns a ResponseEntity containing an Error HttpResponse with the given message, status, and validations.
 *
 * @param status The HTTP status code for the response.
 * @param message The error message.
 * @param validations The validations to be included in the response.
 * @return A ResponseEntity containing an Error HttpResponse.
 */
fun errorResponse(
    status: HttpStatus,
    message: String,
    validations: Any
): ResponseEntity<HttpResponse> {
    // Create an Error HttpResponse with the given parameters
    val errorResponse = Error(status, message, status.reasonPhrase, validations)

    // Return a ResponseEntity with the given status and Error HttpResponse
    return ResponseEntity.status(status).body(errorResponse)
}


/**
 * Generates a validation error response.
 *
 * @param errors A list of FieldError objects.
 * @return A ResponseEntity containing an HttpResponse with validation errors.
 */
fun validationErrorResponse(errors: List<FieldError>): ResponseEntity<HttpResponse> {
    // Map the list of FieldError objects to a list of ErrorFieldResponse objects.
    val fieldResponses: List<ErrorFieldResponse> = errors.map { fieldError ->
        ErrorFieldResponse(fieldError.field, fieldError.defaultMessage!!)
    }

    // Return an error response with the given status, message, and field responses.
    return errorResponse(BAD_REQUEST, "Validation errors", fieldResponses)
}

fun generateRandomCode(length: Int): String {
    val value = StringBuilder(length)
    for (i in 0 until length) {
        value.append(NUMERIC[RANDOM.nextInt(NUMERIC.length)])
    }
    return String(value)
}

/**
 * Generates a random string of the specified length using the characters in ALPHA_NUMERIC.
 *
 * @param length The length of the random string to generate.
 * @return A random string of the specified length.
 */
fun generateRandomString(length: Int): String {
    // Initialize a StringBuilder with the specified length
    val value = StringBuilder(length)

    // Generate random characters and append them to the StringBuilder
    for (i in 0 until length) {
        value.append(ALPHA_NUMERIC[RANDOM.nextInt(ALPHA_NUMERIC.length)])
    }

    // Convert the StringBuilder to a String and return
    return String(value)
}

/**
 * Returns the file extension from the given fileName.
 *
 * If no extension is found or the dot is the last character, an empty string is returned.
 *
 * @param fileName The name of the file.
 * @return The file extension.
 */
fun getFileExtension(fileName: String): String {
    val dotIndex = fileName.lastIndexOf(".")
    return if (dotIndex == -1 || dotIndex == fileName.length - 1) {
        "" // No extension found or it's the last character
    } else fileName.substring(dotIndex + 1)
}

/**
 * Retrieves the file extension from the provided MultipartFile object.
 *
 * If no extension is found or if the dot is the last character, an empty string is returned.
 *
 * @param file The MultipartFile object representing the file.
 * @return The file extension.
 */
fun getFileExtension(file: MultipartFile): String {
    // Get the original filename from the MultipartFile object or set it to an empty string if null
    val fileName = file.originalFilename ?: ""

    // Find the index of the last dot in the filename
    val dotIndex = fileName.lastIndexOf(".")

    // Return the file extension if found, otherwise return an empty string
    return if (dotIndex == -1 || dotIndex == fileName.length - 1) {
        "" // No extension found or it's the last character
    } else fileName.substring(dotIndex + 1)
}

fun convertToDateTime(date: Date, time: Date): Date = try {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.FRANCE)
    val timeFormat = SimpleDateFormat("HH:mm", Locale.FRANCE)
    val datetimeFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.FRANCE)
    val stringDateTime = "${dateFormat.format(date)}T${timeFormat.format(time)}"
    datetimeFormat.parse(stringDateTime)
} catch (e: ParseException) {
    throw Exception("Date format is invalid date");
}

fun generateRandomPasswordString(length: Int): String {
    val value = StringBuilder(length)
    for (i in 0 until length) {
        value.append(ALPHA_NUMERIC_PASSWORD[RANDOM.nextInt(ALPHA_NUMERIC_PASSWORD.length)])
    }
    return String(value)
}

fun Date.format(format: String, locale: Locale = Locale.getDefault()): String {
    val formatter = SimpleDateFormat(format, locale)
    return formatter.format(this)
}

fun String.toDate(format: String, locale: Locale = Locale.getDefault()): Date {
    val formatter = SimpleDateFormat(format, locale)
    return formatter.parse(this)
}

fun Date.toSimpleDate(format: String, locale: Locale = Locale.getDefault()): Date {
    return this.format(format, locale).toDate(format, locale)
}

/**
 * Checks if the given file extension is one of the supported image extensions.
 *
 * @param fileExtension The file extension to check.
 * @return True if the file extension is "jpeg", "jpg", or "png", regardless of case.
 */
fun checkFileExtension(fileExtension: String): Boolean {
    // Check if the file extension is "jpeg", "jpg", or "png", regardless of case.
    return fileExtension.equals("jpeg", ignoreCase = true) ||
            fileExtension.equals("jpg", ignoreCase = true) ||
            fileExtension.equals("png", ignoreCase = true)
}

/**
 * Determines the content type of a file based on its extension.
 *
 * @param fileName The name of the file.
 * @return The content type of the file.
 */
fun determineContentType(fileName: String): MediaType {
    // Get the file extension
    val extension: String = getFileExtension(fileName)

    // Check if the file extension is "pdf"
    if ("pdf".equals(extension, ignoreCase = true)) {
        // If the file extension is "pdf", return the content type for PDF
        return MediaType.APPLICATION_PDF
    }

    // Check if the file extension is a valid image extension
    if (checkFileExtension(extension)) {
        // If the file extension is a valid image extension, return the content type for JPEG
        return MediaType.IMAGE_JPEG
    }

    // Check if the file extension is "svg"
    if ("svg".equals(extension, ignoreCase = true)) {
        // If the file extension is "svg", return the content type for SVG
        return MediaType.parseMediaType("image/svg+xml")
    }

    // If the file extension is not recognized, return the content type for octet stream
    return MediaType.APPLICATION_OCTET_STREAM
}