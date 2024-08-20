package dev.perogroupe.wecheapis.services

import dev.perogroupe.wecheapis.dtos.requests.ContactRequest
import dev.perogroupe.wecheapis.dtos.responses.ContactResponse
import org.springframework.data.domain.Page

interface ContactService {
    fun store(request: ContactRequest): ContactResponse
    fun delete(id: String) : String
    fun show(id: String) : ContactResponse
    fun list() : List<ContactResponse>
    fun listAll(page: Int, size: Int) : Page<ContactResponse>
}