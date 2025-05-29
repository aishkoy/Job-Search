package kg.attractor.job_search.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    private final MessageSource messageSource;

    @Value("${spring.mail.username}")
    private String EMAIL_FROM;

    public void sendEmail(String to, String link) throws MessagingException, IOException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        Locale locale = LocaleContextHolder.getLocale();
        helper.setFrom(EMAIL_FROM, "Job Search Support");
        helper.setTo(to);

        String subject = messageSource.getMessage("email.reset.subject", null, locale);
        String emailContent = loadEmailTemplate(locale);
        emailContent = emailContent.replace("${link}", link);

        helper.setSubject(subject);
        helper.setText(emailContent, true);
        mailSender.send(message);
    }

    private String loadEmailTemplate(Locale locale) throws IOException {
        String language = locale.getLanguage();
        String templatePath = "templates/auth/reset-password-email_" + language + ".html";

        try {
            ClassPathResource resource = new ClassPathResource(templatePath);
            try (Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)) {
                return FileCopyUtils.copyToString(reader);
            }
        } catch (IOException e) {
            ClassPathResource defaultResource = new ClassPathResource("templates/auth/reset-password-email.html");
            try (Reader reader = new InputStreamReader(defaultResource.getInputStream(), StandardCharsets.UTF_8)) {
                return FileCopyUtils.copyToString(reader);
            }
        }
    }
}