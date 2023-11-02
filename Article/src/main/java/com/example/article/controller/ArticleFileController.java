package com.example.article.controller;

import com.example.article.entity.ArticleFile;
import com.example.article.service.ArticleFileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/files/get/{imageName}")
    public ResponseEntity<Resource> getImage(@PathVariable String imageName) {
        try {
            Path imagePath = Paths.get(storagePath).resolve(imageName);
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

    @GetMapping("/files/{id}")
    public ResponseEntity<List<ArticleFile>> geFilesByArticleId(
            @PathVariable("id") Long articleId
    ) {
        try {
            return ResponseEntity.ok( articleFileService.getArticleFileByArticleId(articleId));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(null);
        }
    }


}
