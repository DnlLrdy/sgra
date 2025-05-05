package com.project.sgra.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class EmailService {

    private static final Logger LOGGER = Logger.getLogger(EmailService.class.getName());

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendResetLink(String to, String link) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject("Recuperación de contraseña.");

            String htmlContent = "<html><body>"
                    + "<p>Hola,</p>"
                    + "<p>Para restablecer tu contraseña, haz clic en el siguiente enlace:</p>"
                    + "<p><a href=\"" + link + "\">Restablecer la contraseña</a></p>"
                    + "<p>Este enlace expirará en 15 minutos.</p>"
                    + "</body></html>";

            helper.setText(htmlContent, true);

            mailSender.send(message);
            LOGGER.info("Email de recuperación enviado a: " + to);
        } catch (MessagingException e) {
            LOGGER.severe("Error al enviar el email: " + e.getMessage());
            throw new RuntimeException("No se pudo enviar el email de recuperación.", e);
        }
    }

}
