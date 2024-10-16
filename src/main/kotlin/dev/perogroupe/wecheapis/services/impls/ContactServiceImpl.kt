package dev.perogroupe.wecheapis.services.impls

import dev.perogroupe.wecheapis.dtos.requests.ContactRequest
import dev.perogroupe.wecheapis.dtos.responses.ContactResponse
import dev.perogroupe.wecheapis.entities.Contact
import dev.perogroupe.wecheapis.exceptions.ContactNotFoundException
import dev.perogroupe.wecheapis.repositories.ContactRepository
import dev.perogroupe.wecheapis.services.ContactService
import dev.perogroupe.wecheapis.services.MessageService
import dev.perogroupe.wecheapis.utils.ADMIN_EMAIL
import dev.perogroupe.wecheapis.utils.response
import dev.perogroupe.wecheapis.utils.toContact
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class ContactServiceImpl(
    private val repository: ContactRepository,
    private val messageService: MessageService,
) : ContactService {
    override fun store(request: ContactRequest): ContactResponse {
        val contact = request.toContact()
        messageService.sendMail(
            ADMIN_EMAIL,
            contact.subject,
            concatText(contact)
        )
        return repository.save(contact).response()
    }

    override fun delete(id: String): String = repository.findById(id).map {
        repository.delete(it)
        "Structure deleted successfully"
    }.orElseThrow {
        ContactNotFoundException("Structure not found")
    }

    override fun show(id: String): ContactResponse = repository.findById(id).map {
        it.response()
    }.orElseThrow {
        ContactNotFoundException("Contact not found")
    }

    override fun list(): List<ContactResponse> = repository.findAll(Sort.by(Sort.Direction.ASC, "createdAt")).map {
        it.response()
    }

    override fun listAll(
        page: Int,
        size: Int,
    ): Page<ContactResponse> {
        val pageable = PageRequest.of(page, size)
        return repository.findAll(pageable).map {
            it.response()
        }
    }

    fun concatText(req: Contact): String {
        return """
    Bonjour Administrator,

    Une nouvelle demande de prise de contact a été effectuée sur la plateforme. Voici les informations ci-dessous :

    Nom : ${req.name}
    Email : ${req.email}
    Message : ${req.message}
    Numéro matricule : ${req.serialNumber}
    Numéro de téléphone : ${req.phoneNumber}

    Cordialement,

    L'équipe de Weche
    """.trimIndent()
    }


}