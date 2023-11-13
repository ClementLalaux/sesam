package com.example.texte.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TexteResponseDTO {
    private String contenu;
    private Long position;
    private String page;
    private Long utilisateurId;
}
