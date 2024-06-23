package nus.iss.edu.sg.final_project_backend_resumaid.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    public void sendRegistrationEmail(String toEmail, String username) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom("noreply@resumaid.com");
        msg.setTo(toEmail);
        msg.setSubject("Welcome to Resumaid!");
        msg.setText("Thanks for signing up with Resumaid " + username + "!");
        emailSender.send(msg);
    }
}
