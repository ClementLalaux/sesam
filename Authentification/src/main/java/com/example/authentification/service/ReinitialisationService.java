package com.example.authentification.service;

import com.example.authentification.entity.Reinitialisation;
import com.example.authentification.repository.ReinitialisationRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ReinitialisationService {

    private final ReinitialisationRepository reinitialisationRepository;
    private final JavaMailSender javaMailSender;

    private final UtilisateurService utilisateurService;


    public ReinitialisationService(ReinitialisationRepository reinitialisationRepository, JavaMailSender javaMailSender, UtilisateurService utilisateurService) {
        this.reinitialisationRepository = reinitialisationRepository;
        this.javaMailSender = javaMailSender;
        this.utilisateurService = utilisateurService;
    }

    public void processPasswordResetRequest(String email) {
        String token = generateUniqueToken();

        Reinitialisation reinitialisation = Reinitialisation.builder().
                email(email).token(token).utilise(false).build();

        reinitialisationRepository.save(reinitialisation);

        String resetPasswordLink = "http://localhost:3000/" + token;
        sendEmail(email,resetPasswordLink);
    }

    private String generateUniqueToken() {
        return UUID.randomUUID().toString();
    }

    public boolean sendEmail(String email, String resetLink) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(email);
            helper.setSubject("Réinitialisation de mot de passe");
            helper.setText("Bonjour,\n\nVeuillez cliquer sur le lien suivant pour réinitialiser votre mot de passe : " + resetLink);

            javaMailSender.send(message);
            return true;
        } catch (MailException e) {
            return false;
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean resetPassword(String token, String newPassword) {
        try {
            Optional<Reinitialisation> reinitialisationOptional = reinitialisationRepository.findByToken(token);
            if (reinitialisationOptional.isPresent()) {
                Reinitialisation reinitialisation = reinitialisationOptional.get();
                if (!reinitialisation.isUtilise()) {
                    String email = reinitialisation.getEmail();
                    if(utilisateurService.updatePassword(email,newPassword) != null){
                        reinitialisation.setUtilise(true);
                        reinitialisationRepository.save(reinitialisation);
                        return true;
                    }
                }
                return false;
            } else {
                throw new RuntimeException("Token not found: " + token);
            }
        } catch (Exception ex) {
            throw new RuntimeException("An error occurred: " + ex.getMessage());
        }
    }
}
