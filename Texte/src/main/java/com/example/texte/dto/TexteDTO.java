package com.example.texte.dto;

import com.example.texte.entity.Texte;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TexteDTO {
    private List<Texte> textes;
    private UtilisateurDTO utilisateurDTO;
}
