package com.tracker.service.email;

import com.tracker.dto.email.EmailTemplate;

/**
 * @author by Raj Aryan,
 * created on 06/10/2024
 */
public interface IEmailService {

    void sendEmail(EmailTemplate emailTemplateDTO);

    EmailTemplate getEmailTemplate(Object request);
}
