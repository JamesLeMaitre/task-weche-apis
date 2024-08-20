package dev.perogroupe.wecheapis.services

interface MessageService {
    fun sendMail(email: String, subject: String, message: String)
}