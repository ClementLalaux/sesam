package com.example.article.service;

import com.example.article.entity.ArticleFile;
import com.example.article.repository.ArticleFileRepository;
import com.example.article.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class ArticleFileService {

    private final ArticleFileRepository articleFileRepository;


    private final ArticleRepository articleRepository;

    @Value("${app.file.upload-dir}")
    private String storagePath;

    @Autowired
    public ArticleFileService(ArticleFileRepository articleFileRepository, ArticleRepository articleRepository) {
        this.articleFileRepository = articleFileRepository;
        this.articleRepository = articleRepository;
    }

    public List<ArticleFile> getAllArticleFiles() {
        return (List<ArticleFile>) articleFileRepository.findAll();
    }

    public List<ArticleFile> getAllArticleFilesWhereTypeEqualsImage(Long id, String type){
        if (articleRepository.existsById(id)) {
            return  articleFileRepository.findAllByArticleIdAndTypeContaining(id,type);

        }
        throw new RuntimeException("ArticleFile not found");
    }

    public List<ArticleFile> getAllArticleFilesWhereTypeNotEqualsImage(Long id, String type){
        if (articleRepository.existsById(id)) {
            return  articleFileRepository.findAllByArticleIdAndTypeNotContaining(id,type);

        }
        throw new RuntimeException("ArticleFile not found");
    }

    public Optional<ArticleFile> getArticleFileById(Long id) {
        return articleFileRepository.findById(id);
    }

    public ArticleFile createArticleFile(ArticleFile articleFile) {
        return articleFileRepository.save(articleFile);
    }

    public ArticleFile updateArticleFile(Long id, MultipartFile updatedArticleFile) {
        try{
            Optional<ArticleFile> articleFileOptional = articleFileRepository.findById(id);
            if(articleFileOptional.isPresent()){
                ArticleFile articleFile = articleFileOptional.get();
                Path filePath;
                if(articleFile.getType() != null && articleFile.getType().contains("image")) {
                    filePath = Paths.get(storagePath + "/images/" + articleFile.getFilename());
                }else {
                    filePath = Paths.get(storagePath + "/files/" + articleFile.getFilename());
                }
                if (Files.exists(filePath)) {
                    Files.delete(filePath);
                }

                String updatedFileName = StringUtils.cleanPath(Objects.requireNonNull(updatedArticleFile.getOriginalFilename()));
                String uniqueUpdatedFileName = UUID.randomUUID() + "_" + updatedFileName;
                articleFile.setFilename(uniqueUpdatedFileName);
                articleFile.setType(updatedArticleFile.getContentType());
                articleFileRepository.save(articleFile);

                if(articleFile.getType() != null && articleFile.getType().contains("image")) {
                    String updatedFilePath = storagePath + "/images/" + articleFile.getFilename();
                    Files.copy(updatedArticleFile.getInputStream(), Paths.get(updatedFilePath), StandardCopyOption.REPLACE_EXISTING);
                }else {
                    String updatedFilePath = storagePath + "/files/" + articleFile.getFilename();
                    Files.copy(updatedArticleFile.getInputStream(), Paths.get(updatedFilePath), StandardCopyOption.REPLACE_EXISTING);
                }
                return articleFile;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
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
                if(file.getType() != null && file.getType().contains("image")){
                    Path filePath = Paths.get(storagePath + "/images/"+ file.getFilename());
                    if (Files.exists(filePath)) {
                        Files.delete(filePath);
                    }
                } else if(file.getType() != null){
                    Path filePath = Paths.get(storagePath + "/files/"+ file.getFilename());
                    if (Files.exists(filePath)) {
                        Files.delete(filePath);
                    }
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