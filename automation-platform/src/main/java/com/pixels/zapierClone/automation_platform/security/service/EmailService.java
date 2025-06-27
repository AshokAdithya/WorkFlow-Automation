package com.pixels.zapierClone.automation_platform.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendVerificationEmail(String toEmail, String verificationLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("AutomateX : Verify your email");
        message.setText("Please click the link to verify your email:\n" + verificationLink);
        mailSender.send(message);
    }

    public void sendNotificationForExpiredToken(String toEmail,String serviceName){
        String subject = "AutomateX : Re-authentication Needed";
        String context = "Your credentials for " + serviceName + " have expired. Please sign in again to continue using workflows.";
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(context);
        mailSender.send(message);
    }
}
