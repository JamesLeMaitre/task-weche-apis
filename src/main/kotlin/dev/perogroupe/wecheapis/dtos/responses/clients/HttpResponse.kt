package dev.perogroupe.wecheapis.dtos.responses.clients

import org.springframework.http.HttpStatusCode

sealed class HttpResponse(val status: HttpStatusCode, val message: String) {
    class Success(status: HttpStatusCode, message: String, val data: Any) : HttpResponse(status, message)
    class Error(status: HttpStatusCode, message: String, val reason: String, val validations: Any?) :
        HttpResponse(status, message)
}