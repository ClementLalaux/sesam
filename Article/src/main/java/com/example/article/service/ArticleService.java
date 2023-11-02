package com.example.article.service;

import com.example.article.dto.ArticleResponseDTO;
import com.example.article.dto.UtilisateurDTO;
import com.example.article.entity.Article;
import com.example.article.entity.ArticleFile;
import com.example.article.repository.ArticleFileRepository;
import com.example.article.repository.ArticleRepository;
import com.example.article.tool.RestClient;
import com.example.article.utils.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    @Autowired
    private ArticleFileRepository articleFileRepository;

    @Autowired
    private ArticleFileService articleFileService;

    @Autowired
    private Mapper mapper;

    @Value("${app.file.upload-dir}")
    private String storagePath;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }


    public Article createArticle(Article article){
        Article newArticle = articleRepository.save(article);
        return newArticle;
    }

    public List<Article> getAllArticles(){
        return (List<Article>) articleRepository.findAll();
    }

    public Article getById(Long id){
        if(articleRepository.findById(id).isPresent()){
            Article article = articleRepository.findById(id).get();
            return article;
        }
        throw new RuntimeException("Not found");
    }

    public ArticleResponseDTO updateArticle(Long id, ArticleResponseDTO article){
        RestClient<UtilisateurDTO, String> restClient = new RestClient<>("http://localhost:8081/api/");
        UtilisateurDTO userDTO = restClient.get("auth/"+article.getUtilisateurId(), UtilisateurDTO.class);
        Article article1 = getById(id);
        if(userDTO != null  && article1 !=null){
            article1.setContenu(article.getContenu());
            article1.setId(id);
            article1.setPublication(article.getPublication());
            article1.setTitre(article.getTitre());
            article1.setStatut(article.isStatut());
            article1.setUtilisateurId(article.getUtilisateurId());
            articleRepository.save(article1);
            return mapper.mapToDto(article1);
        }
        throw new RuntimeException("Not found");
    }

    public boolean deleteArticle(Long id){
        Article article = getById(id);
        if(article != null){
            List<ArticleFile> files = articleFileRepository.findAllByArticleId(id);
            if(files != null){
                for (ArticleFile a:files) {
                    articleFileService.deleteArticleFile(a.getId());
                }
            }
            articleRepository.delete(article);
            return true;
        }
        throw new RuntimeException("Error");
    }

    public void addFileToArticle(Long articleId, MultipartFile file) {
        Article article = getById(articleId);
        if (article != null) {
            try {
                String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
                String uniqueFileName = UUID.randomUUID() + "_" + fileName;

                ArticleFile articleFile = new ArticleFile();
                articleFile.setFilename(uniqueFileName);
                articleFile.setArticle(article);
                String fileType = file.getContentType();
                articleFile.setType(fileType);

                // Enregistrez l'instance ArticleFile dans la base de données
                articleFileRepository.save(articleFile);

                // Enregistrez le fichier sur le système de fichiers
                String filePath = storagePath + File.separator + uniqueFileName;
                Files.copy(file.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new RuntimeException("Article not found");
        }
    }

}