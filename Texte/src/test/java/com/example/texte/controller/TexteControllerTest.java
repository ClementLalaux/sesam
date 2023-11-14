package com.example.texte.controller;

import com.example.texte.controller.TexteController;
import com.example.texte.dto.TexteResponseDTO;
import com.example.texte.entity.Texte;
import com.example.texte.service.TexteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class TexteControllerTest {

    @Mock
    private TexteService texteService;

    @InjectMocks
    private TexteController texteController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAll() {
        List<Texte> textes = new ArrayList<>();

        when(texteService.getAllTextes()).thenReturn(textes);

        ResponseEntity<List<Texte>> response = texteController.getAll();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(textes, response.getBody());
    }

    @Test
    void testGetById() {
        Long id = 1L;
        Texte texte = new Texte();
        when(texteService.getById(id)).thenReturn(texte);

        ResponseEntity<Texte> response = texteController.getById(id);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(texte, response.getBody());
    }

    @Test
    void testGetByIdException() {
        Long id = 1L;
        when(texteService.getById(id)).thenThrow(new RuntimeException("Not found"));

        ResponseEntity<Texte> response = texteController.getById(id);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(null, response.getBody());
    }

    @Test
    void testGetByPageException() {
        String page = "somePage";
        when(texteService.getTextesByPage(page)).thenThrow(new RuntimeException("Not found"));

        ResponseEntity<List<Texte>> response = texteController.getByPage(page);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(null, response.getBody());
    }

    @Test
    void testPost() {
        Texte texte = new Texte();
        when(texteService.createTexte(texte)).thenReturn(texte);

        ResponseEntity<Texte> response = texteController.post(texte);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(texte, response.getBody());
    }

    @Test
    void testGetByPage() {
        String page = "somePage";
        List<Texte> textes = new ArrayList<>();

        when(texteService.getTextesByPage(page)).thenReturn(textes);

        ResponseEntity<List<Texte>> response = texteController.getByPage(page);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(textes, response.getBody());
    }

    @Test
    void testDelete() {
        Long id = 1L;

        ResponseEntity<String> response = texteController.delete(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Texte supprimé avec succès", response.getBody());
        verify(texteService, times(1)).deleteTexte(id);
    }

    @Test
    void testUpdate() {
        Long id = 1L;
        TexteResponseDTO texteResponseDTO = new TexteResponseDTO();
        Texte texte = new Texte();
        when(texteService.updateTexte(id, texteResponseDTO)).thenReturn(texte);

        ResponseEntity<Texte> response = texteController.update(texteResponseDTO, id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(texte, response.getBody());
    }

}
