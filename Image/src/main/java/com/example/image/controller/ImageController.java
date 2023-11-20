package com.example.image.controller;

import com.example.image.entity.Image;
import com.example.image.service.ImageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("api/image")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT , RequestMethod.DELETE})
public class ImageController {

    @Value("${app.file.upload-dir}")
    private String storagePath;

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping("/find/{imageName}")
    public ResponseEntity<Resource> getImage(@PathVariable String imageName) {
        try {
            Path imagePath = Paths.get(storagePath+"/").resolve(imageName);
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

    @GetMapping("{id}")
    public ResponseEntity<Image> getImageById(@PathVariable("id") Long articleId) {
        try {
            return ResponseEntity.ok( imageService.getImageById(articleId));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(null);
        }
    }

    @GetMapping("page/{page}")
    public ResponseEntity<List<Image>> getImageByPage(@PathVariable("page") String page) {
        try {
            return ResponseEntity.ok( imageService.getAllImagesByPage(page));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(null);
        }
    }

    @GetMapping("")
    public ResponseEntity<List<Image>> getImageByPage() {
        try {
            return ResponseEntity.ok( imageService.getAllImages());
        } catch (Exception e) {
            return ResponseEntity.status(401).body(null);
        }
    }

    @PostMapping("")
    public ResponseEntity<String> addImage(@RequestParam("file") MultipartFile file,@RequestParam("position") Long position, @RequestParam("page") String page){
        try {
            imageService.addImage(file,position,page);
            return ResponseEntity.status(HttpStatus.CREATED).body("Image modifié avec succès.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur lors de l'ajout du fichier : " + e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteImage(@PathVariable("id") Long id){
        try {
            imageService.deleteImage(id);
            return ResponseEntity.status(HttpStatus.CREATED).body("Image supprimé avec succès.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur lors de l'ajout du fichier : " + e.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<String> updateImage(@PathVariable("id") Long id,@RequestParam("file") MultipartFile file){
        try {
            imageService.updateImage(id,file);
            return ResponseEntity.status(HttpStatus.CREATED).body("Image modifiée avec succès.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur lors de l'ajout du fichier : " + e.getMessage());
        }
    }

}
