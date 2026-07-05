package com.january0001.project.forumbackend.security.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendVerificationCode(String email, String emailCode) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try{
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(email);
            helper.setSubject("Verification code for your Registration at the Forum.");
            helper.setText(buildVerificationEmail(emailCode), true);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("Failed to send verification code. {}", e.getMessage());
        }
    }

    private String buildVerificationEmail(String code) {
        return """
                <!DOCTYPE html>
                        <html>
                        <head><meta charset="UTF-8"></head>
                        <body style="font-family: Arial; text-align: center; padding: 20px;">
                            <h2>Welcome to the Forum!</h2>
                            <p>Your verification code is:</p>
                            <div style="font-size: 32px; font-weight: bold; letter-spacing: 4px;
                                        padding: 15px; background: #f5f5f5; display: inline-block;
                                        border-radius: 8px; margin: 15px 0;">
                                """ + code + """
                            </div>
                            <p>This code expires in <strong>5 minutes</strong>.</p>
                            <p style="color: #777;">If you didn't request this, you may ignore this email.</p>
                        </body>
                        </html>
                """;
    }
}
