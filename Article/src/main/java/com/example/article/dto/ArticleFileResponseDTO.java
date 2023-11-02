package com.example.article.dto;

import com.example.article.entity.Article;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleFileResponseDTO {

    private Long id;
    private String filename;
    private String type;
    private Article article;

}
