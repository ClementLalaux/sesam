package com.example.article.utils;

import com.example.article.dto.ArticleResponseDTO;
import com.example.article.entity.Article;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class Mapper {

    public ArticleResponseDTO mapToDto(Article article){
        ModelMapper mapper = new ModelMapper();

        ArticleResponseDTO articleResponseDTO = mapper.map(article, ArticleResponseDTO.class);
        return articleResponseDTO;
    }

    public Article mapToEntity(ArticleResponseDTO articleResponseDTO){
        ModelMapper mapper = new ModelMapper();

        Article article = mapper.map(articleResponseDTO, Article.class);
        return article;
    }

}
