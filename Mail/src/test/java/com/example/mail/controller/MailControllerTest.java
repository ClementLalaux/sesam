package com.example.mail.controller;

import com.example.mail.controller.MailController;
import com.example.mail.dto.EmailData;
import com.example.mail.service.MailService;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class MailControllerTest {

    @Mock
    private MailService mailService;

    @InjectMocks
    private MailController mailController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testSendEmail_Success() throws MessagingException {
        // Given
        EmailData emailData = new EmailData("recipient@example.com", "Test Subject", "Test Body");
        when(mailService.sendEmail(anyString(), anyString(), anyString())).thenReturn(true);

        // When
        ResponseEntity<String> responseEntity = mailController.sendEmail(emailData);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("E-mail envoyé avec succès !", responseEntity.getBody());
        verify(mailService, times(1)).sendEmail(emailData.getRecipient(), emailData.getSubject(), emailData.getBody());
    }

    @Test
    void testSendEmail_Failure() throws MessagingException {
        // Given
        EmailData emailData = new EmailData("recipient@example.com", "Test Subject", "Test Body");
        when(mailService.sendEmail(anyString(), anyString(), anyString())).thenReturn(false);

        // When
        ResponseEntity<String> responseEntity = mailController.sendEmail(emailData);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("Échec de l'envoi de l'e-mail.", responseEntity.getBody());
        verify(mailService, times(1)).sendEmail(emailData.getRecipient(), emailData.getSubject(), emailData.getBody());
    }
}