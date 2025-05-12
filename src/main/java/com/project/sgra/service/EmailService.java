package com.project.sgra.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;
    private final String fromEmail;

    public EmailService(JavaMailSender mailSender, @Value("${spring.mail.username}") String fromEmail) {
        this.mailSender = mailSender;
        this.fromEmail = fromEmail;
    }

    public void sendResetLink(String to, String link) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject("Recuperación de contraseña");

            String htmlContent = """
                    <html>
                        <body>
                            <p>Hola,</p>
                            <p>Para restablecer tu contraseña, haz clic en el siguiente enlace:</p>
                            <p><a href="%s">Restablecer la contraseña</a></p>
                            <p>Este enlace expirará en 15 minutos.</p>
                        </body>
                    </html>
                    """.formatted(link);

            helper.setText(htmlContent, true);
            mailSender.send(message);

            logger.info("Correo de recuperación enviado a: {}", to);
        } catch (MessagingException e) {
            logger.error("Error al enviar el correo de recuperación a {}: {}", to, e.getMessage(), e);
            throw new IllegalStateException("No se pudo enviar el correo de recuperación.", e);
        }
    }

}
