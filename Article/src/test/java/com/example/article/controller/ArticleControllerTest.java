package com.example.article.controller;

import com.example.article.controller.ArticleController;
import com.example.article.dto.ArticleResponseDTO;
import com.example.article.entity.Article;
import com.example.article.service.ArticleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.mock.web.MockMultipartFile;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ArticleControllerTest {

    @Mock
    private ArticleService articleService;

    @InjectMocks
    private ArticleController articleController;

    @BeforeEach
    void setUp() {
        articleService = mock(ArticleService.class);
        articleController = new ArticleController(articleService);
    }

    private MultipartFile createMockMultipartFile(String fileName) {
        return new MockMultipartFile(fileName, fileName, "text/plain", "Test file content".getBytes());
    }

    @Test
    void testGetAll() {
        List<Article> articles = new ArrayList<>();
        when(articleService.getAllArticles()).thenReturn(articles);

        ResponseEntity<List<Article>> responseEntity = articleController.getAll();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(articles, responseEntity.getBody());
    }

    @Test
    void testPost() {
        Article article = new Article();
        when(articleService.createArticle(article)).thenReturn(article);

        ResponseEntity<Article> responseEntity = articleController.post(article);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(article, responseEntity.getBody());
    }

    @Test
    void testGetByIdValidId() {
        Long id = 1L;
        Article article = new Article();
        when(articleService.getById(id)).thenReturn(article);

        ResponseEntity<Article> responseEntity = articleController.getById(id);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(article, responseEntity.getBody());
    }

    @Test
    void testGetByIdInvalidId() {
        Long id = 1L;
        when(articleService.getById(id)).thenThrow(new RuntimeException());

        ResponseEntity<Article> responseEntity = articleController.getById(id);

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertEquals(null, responseEntity.getBody());
    }

    @Test
    void testDelete() {
        Long id = 1L;

        ResponseEntity<String> responseEntity = articleController.delete(id);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Article supprimé avec succès", responseEntity.getBody());
        verify(articleService, times(1)).deleteArticle(id);
    }

    @Test
    void testUpdate() {
        Long id = 1L;
        ArticleResponseDTO articleResponseDTO = new ArticleResponseDTO();
        when(articleService.updateArticle(id, articleResponseDTO)).thenReturn(articleResponseDTO);

        ResponseEntity<ArticleResponseDTO> responseEntity = articleController.update(articleResponseDTO, id);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(articleResponseDTO, responseEntity.getBody());
    }

    @Test
    void testAddFileToArticle() {
        Long articleId = 1L;
        MultipartFile file = createMockMultipartFile("test.txt");;
        doNothing().when(articleService).addFileToArticle(articleId, file);

        ResponseEntity<String> responseEntity = articleController.addFileToArticle(articleId, file);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals("Fichier ajouté avec succès.", responseEntity.getBody());
    }
}
