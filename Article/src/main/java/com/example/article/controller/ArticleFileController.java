package com.example.article.controller;

import com.example.article.entity.ArticleFile;
import com.example.article.service.ArticleFileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("api/article")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT , RequestMethod.DELETE})
public class ArticleFileController {

    private final ArticleFileService articleFileService;

    @Value("${app.file.upload-dir}")
    private String storagePath;


    public ArticleFileController(ArticleFileService articleFileService) {
        this.articleFileService = articleFileService;

    }

    @GetMapping("/files/get/image/{imageName}")
    public ResponseEntity<Resource> getImage(@PathVariable String imageName) {
        try {
            Path imagePath = Paths.get(storagePath+"/images").resolve(imageName);
            Resource resource = new UrlResource(imagePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.IMAGE_PNG); // Remplacez par le type MIME approprié

                return ResponseEntity.ok()
                        .headers(headers)
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null); // Internal Server Error
        }
    }
    @GetMapping("/files/get/file/{fileName}")
    public ResponseEntity<Resource> getFile(@PathVariable String fileName) {
        try {
            Path filePath = Paths.get(storagePath+"/files").resolve(fileName);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_PDF); // Remplacez par le type MIME approprié

                return ResponseEntity.ok()
                        .headers(headers)
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null); // Internal Server Error
        }
    }

    @GetMapping("/files/{id}")
    public ResponseEntity<List<ArticleFile>> getFilesByArticleId(
            @PathVariable("id") Long articleId
    ) {
        try {
            return ResponseEntity.ok( articleFileService.getAllArticleFilesWhereTypeNotEqualsImage(articleId,"image"));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(null);
        }
    }

    @GetMapping("/image/{id}")
    public ResponseEntity<List<ArticleFile>> getImagesByArticleId(
            @PathVariable("id") Long articleId
    ) {
        try {
            return ResponseEntity.ok( articleFileService.getAllArticleFilesWhereTypeEqualsImage(articleId,"image"));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(null);
        }
    }


}
