package com.example.authentification.service;

import com.example.authentification.entity.Utilisateur;
import com.example.authentification.repository.UtilisateurRepository;
import com.example.authentification.service.UtilisateurService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
class UtilisateurServiceTest {

    @Mock
    private UtilisateurRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UtilisateurService utilisateurService;

    @Test
    void testEnregistrerUtilisateur() {
        String email = "test@example.com";
        String password = "password";
        boolean admin = false;

        Utilisateur utilisateur = new Utilisateur(email, password, admin);
        when(userRepository.save(any(Utilisateur.class))).thenReturn(utilisateur);

        Utilisateur savedUser = utilisateurService.enregistrerUtilisateur(email, password, admin);

        verify(userRepository, times(1)).save(any(Utilisateur.class));

        assertEquals(email, savedUser.getEmail());
    }

    @Test
    void testTrouverParEmail() {

        String email = "test@example.com";
        Utilisateur utilisateur = new Utilisateur(email, "password", false);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(utilisateur));

        Utilisateur foundUser = utilisateurService.trouverParEmail(email);

        assertEquals(email, foundUser.getEmail());
    }

    @Test
    void testTrouverParEmailNotFound() {

        String email = "nonexistent@example.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> utilisateurService.trouverParEmail(email));
    }

    @Test
    void testTrouverParId() {

        Long userId = 1L;
        Utilisateur utilisateur = new Utilisateur(userId,"test@example.com", "password", false);

        when(userRepository.findById(userId)).thenReturn(Optional.of(utilisateur));

        Utilisateur foundUser = utilisateurService.trouverParId(userId);

        assertEquals(userId, foundUser.getId());
    }

    @Test
    void testTrouverParIdNotFound() {

        Long userId = 2L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> utilisateurService.trouverParId(userId));
    }
}