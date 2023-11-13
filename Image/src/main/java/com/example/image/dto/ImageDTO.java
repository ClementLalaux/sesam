package com.example.image.dto;

import com.example.image.entity.Image;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImageDTO {
    private List<Image> images;
    private UtilisateurDTO utilisateurDTO;
}
