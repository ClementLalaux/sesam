package com.example.texte.service;

import com.example.texte.dto.TexteResponseDTO;
import com.example.texte.entity.Texte;
import com.example.texte.repository.TexteRepository;
import com.example.texte.service.TexteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TexteServiceTest {

    @Mock
    private TexteRepository texteRepository;

    @InjectMocks
    private TexteService texteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTexte() {
        Texte texte = new Texte();
        when(texteRepository.save(any())).thenReturn(texte);

        Texte createdTexte = texteService.createTexte(texte);

        assertNotNull(createdTexte);
        verify(texteRepository, times(1)).save(any());
    }

    @Test
    void testGetAllTextes() {
        when(texteRepository.findAll()).thenReturn(Collections.emptyList());

        List<Texte> textes = texteService.getAllTextes();

        assertNotNull(textes);
        assertTrue(textes.isEmpty());
        verify(texteRepository, times(1)).findAll();
    }

    @Test
    void testGetById() {
        Long id = 1L;
        Texte texte = new Texte();
        when(texteRepository.findById(id)).thenReturn(Optional.of(texte));

        Texte retrievedTexte = texteService.getById(id);

        assertNotNull(retrievedTexte);
        verify(texteRepository, times(1)).findById(id);
    }

    @Test
    void testGetTextesByPage() {
        String page = "somePage";
        when(texteRepository.findAllByPage(page)).thenReturn(Collections.emptyList());

        List<Texte> textes = texteService.getTextesByPage(page);

        assertNotNull(textes);
        assertTrue(textes.isEmpty());
        verify(texteRepository, times(1)).findAllByPage(page);
    }

    @Test
    void testUpdateTexte() {
        Long id = 1L;
        TexteResponseDTO texteResponseDTO = new TexteResponseDTO();
        texteResponseDTO.setUtilisateurId(1L);
        Texte texte = new Texte();
        when(texteRepository.findById(id)).thenReturn(Optional.of(texte));

        Texte updatedTexte = texteService.updateTexte(id, texteResponseDTO);

        assertNotNull(updatedTexte);
        verify(texteRepository, times(1)).findById(id);
        verify(texteRepository, times(1)).save(any());
    }

    @Test
    void testDeleteTexte() {
        Long id = 1L;
        Texte texte = new Texte();
        when(texteRepository.findById(id)).thenReturn(Optional.of(texte));

        boolean isDeleted = texteService.deleteTexte(id);

        assertTrue(isDeleted);
        verify(texteRepository, times(1)).findById(id);
        verify(texteRepository, times(1)).delete(texte);
    }
}
