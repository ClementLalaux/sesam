package com.example.article.controller;

import com.example.article.dto.ArticleResponseDTO;
import com.example.article.entity.Article;
import com.example.article.service.ArticleService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/article")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT , RequestMethod.DELETE})
public class ArticleController {

    private final ArticleService articleService;


    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;

    }

    @GetMapping("")
    public ResponseEntity<List<Article>> getAll(){
        return ResponseEntity.ok(articleService.getAllArticles());
    }

    @PostMapping("")
    public ResponseEntity<Article> post(@RequestBody Article article){
            Article article1 = articleService.createArticle(article);
            return ResponseEntity.ok(article1);
        }
    @GetMapping("{id}")
    public ResponseEntity<Article> getById(@PathVariable(value = "id") Long id){
        try {
            Article article = articleService.getById(id);
            return ResponseEntity.ok(article);
        } catch (Exception e) {
            return ResponseEntity.status(401).body(null);
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable(value="id") Long id){
        articleService.deleteArticle(id);
        return new ResponseEntity<>("Article supprimé avec succès", HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<ArticleResponseDTO> update(@RequestBody ArticleResponseDTO article, @PathVariable(value = "id") Long id){
        ArticleResponseDTO articleResponseDTO = articleService.updateArticle(id,article);
        return ResponseEntity.ok(articleResponseDTO);
    }

    @PostMapping("/files/{id}")
    public ResponseEntity<String> addFileToArticle(
            @PathVariable("id") Long articleId,
            @RequestParam("file") MultipartFile file
    ) {
        try {
            articleService.addFileToArticle(articleId, file);
            return ResponseEntity.status(HttpStatus.CREATED).body("Fichier ajouté avec succès.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur lors de l'ajout du fichier : " + e.getMessage());
        }
    }


}
