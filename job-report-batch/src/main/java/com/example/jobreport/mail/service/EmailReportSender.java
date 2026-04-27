package com.example.jobreport.mail.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class EmailReportSender {

    private static final Logger log = LoggerFactory.getLogger(EmailReportSender.class);

    private final JavaMailSender mailSender;
    private final Environment environment;

    public EmailReportSender(JavaMailSender mailSender, Environment environment) {
        this.mailSender = mailSender;
        this.environment = environment;
    }

    public void sendHtmlReport(String subject, String htmlBody) {
        String host = environment.getProperty("MAIL_HOST");
        String from = environment.getProperty("MAIL_FROM");
        String to = environment.getProperty("MAIL_TO");

        if (!StringUtils.hasText(host) || !StringUtils.hasText(from) || !StringUtils.hasText(to)) {
            log.info("Mail configuration is incomplete. Report will be logged instead of emailed.");
            log.info("Subject: {}\n{}", subject, htmlBody);
            return;
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);
            mailSender.send(message);
            log.info("Daily job report email sent to {}", to);
        } catch (MessagingException | RuntimeException ex) {
            log.warn("Failed to send email. Report will be logged instead.", ex);
            log.info("Subject: {}\n{}", subject, htmlBody);
        }
    }
}
