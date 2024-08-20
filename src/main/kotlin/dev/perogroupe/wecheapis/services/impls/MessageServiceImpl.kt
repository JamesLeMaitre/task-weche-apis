package dev.perogroupe.wecheapis.services.impls

import dev.perogroupe.wecheapis.services.MessageService
import jakarta.mail.internet.MimeMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service

@Service
class MessageServiceImpl(
    private val mailSender: JavaMailSender
): MessageService {

    /**
     * Sends an email with the provided details.
     *
     * @param email the recipient's email address
     * @param subject the subject of the email
     * @param message the content of the email
     */
    override fun sendMail(email: String, subject: String, message: String) {
        // Create a MimeMessage object
        val mimeMessage: MimeMessage = mailSender.createMimeMessage()

        // Initialize a MimeMessageHelper with UTF-8 encoding
        val helper = MimeMessageHelper(mimeMessage, true, "UTF-8")

        // Set the recipient's email address
        helper.setTo(email)

        // Set the subject of the email
        helper.setSubject(subject)

        // Set the content of the email, allowing HTML
        helper.setText(message, true)

        // Set the sender's email address
        helper.setFrom("assistance.library.platform@gmail.com")

        // Send the email
        mailSender.send(mimeMessage)
    }

}