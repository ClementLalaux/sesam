package com.example.article.service;

import com.example.article.entity.ArticleFile;
import com.example.article.repository.ArticleFileRepository;
import com.example.article.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class ArticleFileService {

    private final ArticleFileRepository articleFileRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Value("${app.file.upload-dir}")
    private String storagePath;

    @Autowired
    public ArticleFileService(ArticleFileRepository articleFileRepository) {
        this.articleFileRepository = articleFileRepository;
    }

    public List<ArticleFile> getAllArticleFiles() {
        return (List<ArticleFile>) articleFileRepository.findAll();
    }

    public Optional<ArticleFile> getArticleFileById(Long id) {
        return articleFileRepository.findById(id);
    }

    public ArticleFile createArticleFile(ArticleFile articleFile) {
        return articleFileRepository.save(articleFile);
    }

    public ArticleFile updateArticleFile(Long id, ArticleFile updatedArticleFile) {
        if (articleFileRepository.existsById(id)) {
            updatedArticleFile.setId(id);
            return articleFileRepository.save(updatedArticleFile);
        }
        throw new RuntimeException("ArticleFile not found");
    }

    public List<ArticleFile> getArticleFileByArticleId(Long id) {
        if (articleRepository.existsById(id)) {
            return articleFileRepository.findAllByArticleId(id);

        }
        throw new RuntimeException("ArticleFile not found");
    }

    public void deleteArticleFile(Long fileId) {
        ArticleFile file = articleFileRepository.findById(fileId).orElse(null);
        if (file != null) {
            try {
                Path filePath = Paths.get(storagePath + "/"+ file.getFilename());
                if (Files.exists(filePath)) {
                    Files.delete(filePath);
                }
                articleFileRepository.delete(file);
            } catch (IOException e) {
                throw new RuntimeException("Error deleting file");
            }
        } else {
            throw new RuntimeException("File not found");
        }
    }
}