package com.example.article.service;

import com.example.article.dto.ArticleResponseDTO;
import com.example.article.entity.Article;
import com.example.article.repository.ArticleFileRepository;
import com.example.article.repository.ArticleRepository;
import com.example.article.utils.Mapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@ContextConfiguration
@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class ArticleServiceTest {
    public static List<Article> articleList;

    @Mock
    private ArticleFileService articleFileService;

    @Mock
    private ArticleFileRepository articleFileRepository;

    @Autowired
    private ArticleService articleService;

    @Mock
    private ArticleRepository articleRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Mapper mapper = new Mapper();
        articleService = new ArticleService(articleRepository, articleFileRepository, articleFileService, mapper);
        articleList = Stream.of(new Article(1L,"test","2023-01-04","test",true,1L),
                new Article(2L,"test","2023-01-04","test",true,1L)).toList();
    }

    @Test
    public void testGetAllArticles() {
        List<Article> articles = new ArrayList<>(); // Créez une liste d'articles factice

        Mockito.when(articleRepository.findAll()).thenReturn(articles);

        List<Article> result = articleService.getAllArticles();

        // Vérifiez si la liste retournée est la même que celle attendue
        assertEquals(articles, result);
    }

    @Test
    void testCreateExpectArticle() {
        Article article = Article.builder()
                .titre("test")
                .publication("2000-01-01")
                .contenu("test")
                .statut(true)
                .utilisateurId(1L)
                .build();

        Article articleCreated = Article.builder()
                .id(1L)
                .titre("test")
                .publication("2000-01-01")
                .contenu("test")
                .statut(true)
                .utilisateurId(1L)
                .build();

        Mockito.when(articleRepository.save(article)).thenReturn(articleCreated);

        Article result = articleService.createArticle(article);

        Assertions.assertEquals(articleCreated, result);
    }

    @Test
    public void testGetById() {
        Long articleId = 1L; // ID factice

        Mockito.when(articleRepository.findById(articleId)).thenReturn(Optional.of(articleList.get(0)));

        Article article = articleService.getById(articleId);

        assertEquals(article, articleList.get(0));
    }

    @Test
    public void testGetByIdWhenNotExist() {
        Long articleId = 1L; // ID factice

        Optional<Article> optionalArticle = Optional.empty();

        Mockito.when(articleRepository.findById(articleId)).thenReturn(optionalArticle);
        Assertions.assertThrows(RuntimeException.class,()->articleService.getById(articleId));
    }

    @Test
    public void testUpdateArticleWithId(){

        ArticleResponseDTO article = ArticleResponseDTO.builder()
                .titre("t")
                .publication("2000-01-01")
                .contenu("test")
                .statut(true)
                .utilisateurId(1L)
                .build();

        Article articleCreated = Article.builder()
                .id(1L)
                .titre("test")
                .publication("2000-01-01")
                .contenu("test")
                .statut(true)
                .utilisateurId(1L)
                .build();

        Optional<Article> optionalArticle = Optional.of(articleCreated);

        Article articleUpdated = Article.builder()
                .id(1L)
                .titre("t")
                .publication("2000-01-01")
                .contenu("test")
                .statut(true)
                .utilisateurId(1L)
                .build();

        ArticleResponseDTO articleResponseDTO = ArticleResponseDTO.builder()
                .titre("t")
                .publication("2000-01-01")
                .contenu("test")
                .statut(true)
                .utilisateurId(1L)
                .build();

        Mockito.when(articleRepository.findById(1L)).thenReturn(optionalArticle);
        Mockito.when(articleRepository.save(articleUpdated)).thenReturn(articleUpdated);
        Assertions.assertEquals(articleResponseDTO,articleService.updateArticle(1L,article));
    }

    @Test
    public void testDeleteArticle() {
        Long articleId = 1L; // ID factice

        Article article = Article.builder()
                .id(articleId)
                .titre("test")
                .publication("2000-01-01")
                .contenu("test")
                .statut(true)
                .utilisateurId(1L)
                .build();

        Mockito.when(articleRepository.findById(articleId)).thenReturn(Optional.of(article));

        boolean deleted = articleService.deleteArticle(articleId);

        assertTrue(deleted);
        Mockito.verify(articleFileRepository, Mockito.times(1)).findAllByArticleId(articleId);
        Mockito.verify(articleFileService, Mockito.times(1)).deleteArticleFile(Mockito.anyLong());
        Mockito.verify(articleRepository, Mockito.times(1)).delete(article);
    }

    @Test
    public void testAddFileToArticle() {
        Long articleId = 1L; // ID factice

        Article article = Article.builder()
                .id(articleId)
                .titre("test")
                .publication("2000-01-01")
                .contenu("test")
                .statut(true)
                .utilisateurId(1L)
                .build();

        MultipartFile file = createMockMultipartFile("test.txt");

        Mockito.when(articleRepository.findById(articleId)).thenReturn(Optional.of(article));

        Assertions.assertDoesNotThrow(() -> articleService.addFileToArticle(articleId, file));
    }

    // Méthode utilitaire pour créer un MultipartFile factice
    private MultipartFile createMockMultipartFile(String fileName) {
        return new MockMultipartFile(fileName, fileName, "text/plain", "Test file content".getBytes());
    }
}