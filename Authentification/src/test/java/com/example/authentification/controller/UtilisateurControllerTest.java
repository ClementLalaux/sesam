package com.example.authentification.controller;

import com.example.authentification.controller.UtilisateurController;
import com.example.authentification.dto.LoginRequestDTO;
import com.example.authentification.dto.LoginResponseDTO;
import com.example.authentification.dto.RegisterRequestDTO;
import com.example.authentification.dto.RegisterResponseDTO;
import com.example.authentification.entity.Utilisateur;
import com.example.authentification.security.JWTGenerator;
import com.example.authentification.service.UtilisateurService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UtilisateurControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UtilisateurService utilisateurService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JWTGenerator generator;

    @InjectMocks
    private UtilisateurController utilisateurController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLogin() {

        LoginRequestDTO loginRequestDTO = new LoginRequestDTO("example@example.com", "password");

        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);

        Utilisateur utilisateur = new Utilisateur(/* Ajouter les valeurs appropriées pour l'utilisateur */);
        when(utilisateurService.trouverParEmail(loginRequestDTO.getEmail())).thenReturn(utilisateur);

        when(generator.generateToken(authentication)).thenReturn("jwt-token");

        ResponseEntity<LoginResponseDTO> response = utilisateurController.login(loginRequestDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());

    }

    @Test
    void testRegister() {
        RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO("example@example.com", "password", false);

        Utilisateur utilisateur = new Utilisateur(/* Ajouter les valeurs appropriées pour l'utilisateur */);
        when(utilisateurService.enregistrerUtilisateur(anyString(), anyString(), anyBoolean())).thenReturn(utilisateur);

        ResponseEntity<RegisterResponseDTO> response = utilisateurController.register(registerRequestDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGet() {
        Long userId = 1L;

        Utilisateur utilisateur = new Utilisateur();
        when(utilisateurService.trouverParId(userId)).thenReturn(utilisateur);

        ResponseEntity<Utilisateur> response = utilisateurController.get(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());

    }

}