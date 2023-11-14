package com.example.article.service;

import com.example.article.entity.Article;
import com.example.article.entity.ArticleFile;
import com.example.article.repository.ArticleFileRepository;
import com.example.article.repository.ArticleRepository;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

class ArticleFileServiceTest {

    @Mock
    private ArticleFileRepository articleFileRepository;

    @InjectMocks
    private ArticleFileService articleFileService;

    @Mock
    private ArticleRepository articleRepository;

    @Value("${app.file.upload-dir}")
    private String storagePath;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        articleFileService = new ArticleFileService(articleFileRepository, articleRepository);
    }

    @Test
    void testGetAllArticleFiles() {
        Article article = Article.builder().titre("test").publication("2000-01-01").contenu("test").statut(true).utilisateurId(1L).build();
        List<ArticleFile> expectedArticleFiles = new ArrayList<>();
        expectedArticleFiles.add(new ArticleFile(1L,"test","image",article));
        expectedArticleFiles.add(new ArticleFile(2L,"test2","image",article));

        when(articleFileRepository.findAll()).thenReturn(expectedArticleFiles);

        List<ArticleFile> result = articleFileService.getAllArticleFiles();

        assertEquals(expectedArticleFiles, result);
    }

    @Test
    void testGetAllArticleFilesWhereTypeEqualsImage() {

        Long id = 1L;
        String type = "image";
        List<ArticleFile> expectedArticleFiles = new ArrayList<>();
        expectedArticleFiles.add(new ArticleFile());

        when(articleRepository.existsById(id)).thenReturn(true);
        when(articleFileRepository.findAllByArticleIdAndTypeContaining(id, type)).thenReturn(expectedArticleFiles);

        List<ArticleFile> result = articleFileService.getAllArticleFilesWhereTypeEqualsImage(id, type);

        assertEquals(expectedArticleFiles, result);
    }

    @Test
    void testGetAllArticleFilesWhereTypeNotEqualsImage() {

        Long id = 1L;
        String type = "text";
        List<ArticleFile> expectedArticleFiles = new ArrayList<>();
        expectedArticleFiles.add(new ArticleFile());

        when(articleRepository.existsById(id)).thenReturn(true);
        when(articleFileRepository.findAllByArticleIdAndTypeNotContaining(id, type)).thenReturn(expectedArticleFiles);

        List<ArticleFile> result = articleFileService.getAllArticleFilesWhereTypeNotEqualsImage(id, type);

        assertEquals(expectedArticleFiles, result);
    }

    @Test
    void testGetArticleFileById() {
        Long id = 1L;
        ArticleFile expectedArticleFile = new ArticleFile();

        when(articleFileRepository.findById(id)).thenReturn(Optional.of(expectedArticleFile));

        Optional<ArticleFile> result = articleFileService.getArticleFileById(id);

        assertTrue(result.isPresent());
        assertEquals(expectedArticleFile, result.get());
    }

    @Test
    void testUpdateArticleFile() {

        Long id = 1L;
        ArticleFile updatedArticleFile = new ArticleFile(/* add necessary fields for initialization */);
        updatedArticleFile.setId(id);

        when(articleFileRepository.existsById(id)).thenReturn(true);
        when(articleFileRepository.save(updatedArticleFile)).thenReturn(updatedArticleFile);

        ArticleFile result = articleFileService.updateArticleFile(id, updatedArticleFile);

        assertEquals(updatedArticleFile, result);
    }

    @Test
    void testGetArticleFileByArticleId() {

        Long id = 1L;
        List<ArticleFile> expectedArticleFiles = new ArrayList<>();
        expectedArticleFiles.add(new ArticleFile(/* add necessary fields for initialization */));

        when(articleRepository.existsById(id)).thenReturn(true);
        when(articleFileRepository.findAllByArticleId(id)).thenReturn(expectedArticleFiles);

        List<ArticleFile> result = articleFileService.getArticleFileByArticleId(id);

        assertEquals(expectedArticleFiles, result);
    }

    @Test
    void testDeleteArticleFile() {

        Long fileId = 1L;
        ArticleFile articleFile = new ArticleFile();

        when(articleFileRepository.findById(fileId)).thenReturn(Optional.of(articleFile));
        when(articleFileRepository.existsById(fileId)).thenReturn(true);
        when(articleFileRepository.findById(fileId)).thenReturn(Optional.of(articleFile));

        assertDoesNotThrow(() -> articleFileService.deleteArticleFile(fileId));

        Mockito.verify(articleFileRepository, times(1)).delete(articleFile);
    }

}
