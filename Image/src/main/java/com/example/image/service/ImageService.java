package com.example.image.service;

import com.example.image.entity.Image;
import com.example.image.repository.ImageRepository;
import jakarta.persistence.Access;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class ImageService {

    private final ImageRepository imageRepository;
    private String storagePath;


    public ImageService(ImageRepository imageRepository, @Value("${app.file.upload-dir}") String storagePath) {
        this.imageRepository = imageRepository;
        this.storagePath = storagePath;
    }

    public void setStoragePath(String path) {
        this.storagePath = path;
    }

    public String getStoragePath() {
        return storagePath;
    }

    public List<Image> getAllImages(){
        return (List<Image>) imageRepository.findAll();
    }

    public List<Image> getAllImagesByPage(String page){
        List<Image> imageList = imageRepository.findAllByPageOrderByPosition(page);
        if(imageList != null){
            return imageList;
        }
        throw new RuntimeException("Not found");
    }

    public Image getImageById(Long id){
        Optional<Image> imageOptional = imageRepository.findById(id);
        if(imageOptional.isPresent()){
            return imageOptional.get();
        }
        throw new RuntimeException("Not found");
    }

    public Image addImage(MultipartFile image, Long position , String page){
        try {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(image.getOriginalFilename()));
            String uniqueFileName = UUID.randomUUID() + "_" + fileName;

            Image image1 = Image.builder()
                    .filename(uniqueFileName)
                    .type(image.getContentType())
                    .position(position)
                    .page(page)
                    .build();
            imageRepository.save(image1);
            String storagePathHere = getStoragePath();
            String filePath = storagePathHere + File.separator + uniqueFileName;
            Files.copy(image.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
            return image1;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean deleteImage(Long id) {
        Optional<Image> image = imageRepository.findById(id);
        if(image.isPresent()){
            try {
                Path filePath = Paths.get(storagePath + File.separator + image.get().getFilename());
                if(Files.exists(filePath)){
                    Files.delete(filePath);
                    imageRepository.delete(image.get());
                    return true;
                } else {
                    imageRepository.delete(image.get());
                    return false;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new RuntimeException("Image not found");
        }
    }

    public Image updateImage(Long id, MultipartFile updatedImage) {
        try {
            Optional<Image> optionalImage = imageRepository.findById(id);

            if (optionalImage.isPresent()) {
                Image existingImage = optionalImage.get();

                Path oldFilePath = Paths.get(storagePath + File.separator + existingImage.getFilename());
                if (Files.exists(oldFilePath)) {
                    Files.delete(oldFilePath);
                }

                String updatedFileName = StringUtils.cleanPath(Objects.requireNonNull(updatedImage.getOriginalFilename()));
                String uniqueUpdatedFileName = UUID.randomUUID() + "_" + updatedFileName;

                existingImage.setFilename(uniqueUpdatedFileName);
                existingImage.setType(updatedImage.getContentType());

                imageRepository.save(existingImage);
                
                String updatedFilePath = storagePath + File.separator + uniqueUpdatedFileName;
                Files.copy(updatedImage.getInputStream(), Paths.get(updatedFilePath), StandardCopyOption.REPLACE_EXISTING);

                return existingImage;
            } else {
                throw new RuntimeException("Image not found");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
