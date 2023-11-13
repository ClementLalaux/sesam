package com.example.article;

import com.example.article.dto.ArticleResponseDTO;
import com.example.article.entity.Article;
import com.example.article.repository.ArticleRepository;
import com.example.article.service.ArticleService;
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
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ContextConfiguration
@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class ArticleServiceTest {
    public static List<Article> articleList;

    @Autowired
    private ArticleService articleService;

    @Mock
    private ArticleRepository articleRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Mapper mapper = new Mapper();
        articleService = new ArticleService(articleRepository, mapper);
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
}