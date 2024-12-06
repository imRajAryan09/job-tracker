package com.tracker.service.email;

import com.tracker.dto.email.EmailTemplate;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;

/**
 * @author by Raj Aryan,
 * created on 06/10/2024
 */
@Slf4j
@RequiredArgsConstructor
public abstract class EmailService implements IEmailService {

    private final JavaMailSender mailSender;

    private final SpringTemplateEngine templateEngine;

    @Override
    @Async("asyncExecutor")
    public void sendEmail(EmailTemplate emailTemplate) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(
                    mimeMessage,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name()
            );

            String html = templateEngine.process(emailTemplate.getTemplateName(), emailTemplate.getContext());
            helper.setTo(emailTemplate.getTo());
            helper.setText(html, true);
            helper.setSubject(emailTemplate.getSubject());
            helper.setFrom(emailTemplate.getFrom());
            if (!emailTemplate.getCc().isEmpty()) {
                helper.setCc(emailTemplate.getCc().toArray(new String[0]));
            }
            mailSender.send(mimeMessage);

        } catch (Exception e) {
            log.error("Error occurred while sending email with email template {}", emailTemplate.getTemplateName(), e);
        }
    }
}
