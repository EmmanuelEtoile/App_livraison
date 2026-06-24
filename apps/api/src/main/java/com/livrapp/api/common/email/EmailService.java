package com.livrapp.api.common.email;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Envoi d'e-mails. En dev (app.mail.enabled=false), les e-mails sont
 * simplement journalisés au lieu d'être envoyés — l'appli tourne sans SMTP.
 */
@Slf4j
@Service
public class EmailService {

    private final ObjectProvider<JavaMailSender> mailSenderProvider;

    @Value("${app.mail.from:no-reply@livrapp.cm}")
    private String from;

    @Value("${app.mail.enabled:false}")
    private boolean enabled;

    public EmailService(ObjectProvider<JavaMailSender> mailSenderProvider) {
        this.mailSenderProvider = mailSenderProvider;
    }

    public void send(String to, String subject, String body) {
        if (!enabled) {
            log.info("[MAIL désactivé] À: {} | Sujet: {}\n{}", to, subject, body);
            return;
        }
        JavaMailSender sender = mailSenderProvider.getIfAvailable();
        if (sender == null) {
            log.warn("Aucun JavaMailSender configuré — e-mail non envoyé à {}", to);
            return;
        }
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            sender.send(message);
            log.info("E-mail envoyé à {}", to);
        } catch (Exception e) {
            log.error("Échec de l'envoi d'e-mail à {} : {}", to, e.getMessage());
        }
    }
}
