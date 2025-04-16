package com.alievisa.service

import com.alievisa.utils.CustomLogger
import java.util.*
import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage


interface MailService {

    fun sendMessage(destinationMail: String, code: String)
}

class SMTPService : MailService {

    private val senderMail = System.getenv("MAIL_SENDER") ?: throw IllegalStateException("MAIL_SENDER is not set")
    private val senderPassword = System.getenv("MAIL_SENDER_PASSWORD") ?: throw IllegalStateException("MAIL_SENDER_PASSWORD is not set")
    private val props = Properties().apply {
        put("mail.smtp.auth", "true")
        put("mail.smtp.starttls.enable", "true")
        put("mail.smtp.host", "smtp.mail.ru")
        put("mail.smtp.port", "587")
    }
    private val session = Session.getInstance(props, object : Authenticator() {
        override fun getPasswordAuthentication(): PasswordAuthentication {
            return PasswordAuthentication(senderMail, senderPassword)
        }
    })

    override fun sendMessage(destinationMail: String, code: String) {
        try {

            val subject = "Аутентификация Бёргер Стейк"
            val body = "Код подтверждения: $code\nНикому его не сообщайте"

            val message = MimeMessage(session).apply {
                setFrom(InternetAddress(senderMail))
                setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinationMail))
                setSubject(subject)
                setText(body)
            }

            Transport.send(message)
            CustomLogger.log("SMTPService: Message sent successful $destinationMail")

        } catch (e: Exception) {
            CustomLogger.log("SMTPService: Failed to send mail message - ${e.message}")
            e.printStackTrace()
        }
    }
}
