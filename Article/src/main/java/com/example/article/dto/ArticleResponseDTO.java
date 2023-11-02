package com.example.article.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleResponseDTO {
    private String titre;
    private String publication;
    private String contenu;
    private boolean statut;
    private Long utilisateurId;
    private String filename;
    private String filepath;
}
