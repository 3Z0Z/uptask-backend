package com.up_task_project.uptask_backend.config;

import com.up_task_project.uptask_backend.exception.exceptions.SendEmailException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class MailManager {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String username;

    @Async
    public void sendMessage(String subject, String email, Map<String,Object> variables, String template) {
        if (subject == null || email == null || variables == null || template == null) {
            log.error("-->EmailManager: One or more required parameters are null");
            throw new IllegalArgumentException("Required parameters cannot be null");
        }
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(this.username);
            helper.setTo(email);
            helper.setSubject(subject);

            Context context = new Context();
            context.setVariables(variables);
            String htmlContent = templateEngine.process(template, context);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info("-->EmailManager: Email sent successfully to {}", email);
        } catch(MessagingException e) {
            log.error("-->EmailManager: Error sending email to {}: {}", email, e.getMessage());
            throw new SendEmailException(e.getMessage());
        }
    }

}
