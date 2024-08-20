package dev.perogroupe.wecheapis.handlers

import dev.perogroupe.wecheapis.exceptions.*
import dev.perogroupe.wecheapis.dtos.responses.clients.HttpResponse.*
import dev.perogroupe.wecheapis.dtos.responses.clients.HttpResponse
import dev.perogroupe.wecheapis.utils.errorResponse
import dev.perogroupe.wecheapis.utils.validationErrorResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus.*
import org.springframework.http.ResponseEntity
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.NoHandlerFoundException
import java.io.IOException
import java.util.*

@RestControllerAdvice
class ExceptionsHandler {
    companion object {
        val logs: Logger = LoggerFactory.getLogger(ExceptionsHandler::class.java)
    }

    private val METHOD_IS_NOT_ALLOWED =
        "Cette méthode de demande n'est pas autorisée sur ce point de terminaison. Veuillez envoyer une demande %s"


    @ExceptionHandler(NoHandlerFoundException::class)
    fun noHandlerFoundException(exception: NoHandlerFoundException): ResponseEntity<Error> {
        logs.info("{}", exception.message)
        return errorResponse(BAD_REQUEST, exception.message!!)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun methodArgumentNotValidException(exception: MethodArgumentNotValidException): ResponseEntity<HttpResponse> {
        println(exception.allErrors)
        return validationErrorResponse(exception.fieldErrors)
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    fun methodNotSupportedException(exception: HttpRequestMethodNotSupportedException): ResponseEntity<HttpResponse.Error> {
        val httpMethod = Objects.requireNonNull(exception.supportedHttpMethods).iterator().next()
        return errorResponse(METHOD_NOT_ALLOWED, String.format(METHOD_IS_NOT_ALLOWED, httpMethod))
    }

    @ExceptionHandler(IOException::class)
    fun ioException(exception: IOException): ResponseEntity<Error> {
        logs.info("{}", exception.message)
        return errorResponse(INTERNAL_SERVER_ERROR, exception.message!!)
    }

    @ExceptionHandler(Exception::class)
    fun internalServerErrorException(exception: Exception): ResponseEntity<Error> {
        logs.info("{}", exception.message)
        return errorResponse(INTERNAL_SERVER_ERROR, exception.message!!)
    }

    @ExceptionHandler(RuntimeException::class)
    fun internalServerErrorException(exception: RuntimeException): ResponseEntity<Error> {
        logs.info("{}", exception.message)
        return errorResponse(INTERNAL_SERVER_ERROR, exception.message!!)
    }

    @ExceptionHandler(UserNotFoundException::class)
    fun userNotFoundException(exception: UserNotFoundException): ResponseEntity<Error> {
        logs.info("{}", exception.message)
        return errorResponse(NOT_FOUND, exception.message!!)
    }

    @ExceptionHandler(RoleNotFoundException::class)
    fun roleNotFoundException(exception: RoleNotFoundException): ResponseEntity<Error> {
        logs.info("{}", exception.message)
        return errorResponse(NOT_FOUND, exception.message!!)
    }

    @ExceptionHandler(StructureNotFoundException::class)
    fun structureNotFoundException(exception: StructureNotFoundException): ResponseEntity<Error> {
        logs.info("{}", exception.message)
        return errorResponse(NOT_FOUND, exception.message!!)
    }

    @ExceptionHandler(HelloRetirementNotFoundException::class)
    fun helloRetirementNotFoundException(exception: HelloRetirementNotFoundException): ResponseEntity<Error> {
        logs.info("{}", exception.message)
        return errorResponse(NOT_FOUND, exception.message!!)
    }

    @ExceptionHandler(NewRequestNotFoundException::class)
    fun newRequestNotFoundException(exception: NewRequestNotFoundException): ResponseEntity<Error> {
        logs.info("{}", exception.message)
        return errorResponse(NOT_FOUND, exception.message!!)
    }

    @ExceptionHandler(RejectedRequestNotFoundException::class)
    fun rejectedRequestNotFoundException(exception: RejectedRequestNotFoundException): ResponseEntity<Error> {
        logs.info("{}", exception.message)
        return errorResponse(NOT_FOUND, exception.message!!)
    }

    @ExceptionHandler(PendingRequestNotFoundException::class)
    fun pendingRequestNotFoundException(exception: PendingRequestNotFoundException): ResponseEntity<Error> {
        logs.info("{}", exception.message)
        return errorResponse(NOT_FOUND, exception.message!!)
    }


    @ExceptionHandler(FileStorageException::class)
    fun fileStorageException(exception: FileStorageException): ResponseEntity<Error> {
        logs.info("{}", exception.message)
        return errorResponse(NOT_FOUND, exception.message!!)
    }


    @ExceptionHandler(CheckRequestStatusException::class)
    fun checkStatusException(exception: CheckRequestStatusException): ResponseEntity<Error> {
        logs.info("{}", exception.message)
        return errorResponse(NOT_FOUND, exception.message!!)
    }


    @ExceptionHandler(ApprovedRequestNotFoundException::class)
    fun approvedRequestNotFoundException(exception: ApprovedRequestNotFoundException): ResponseEntity<Error> {
        logs.info("{}", exception.message)
        return errorResponse(NOT_FOUND, exception.message!!)
    }

}