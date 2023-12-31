package com.example.mail.controller;

import com.example.mail.dto.EmailData;
import com.example.mail.service.MailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/mail")
@RestController
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
public class MailController {


    private final MailService emailService;

    public MailController(MailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("send")
    public ResponseEntity<String> sendEmail(@RequestBody EmailData emailData) {
        try {

            String to = emailData.getRecipient();
            String subject = emailData.getSubject();
            String text = emailData.getBody();
            if(emailService.sendEmail(to, subject, text)){
                return ResponseEntity.ok("E-mail envoyé avec succès !");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Échec de l'envoi de l'e-mail.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Échec de l'envoi de l'e-mail.");
        }
    }
}
