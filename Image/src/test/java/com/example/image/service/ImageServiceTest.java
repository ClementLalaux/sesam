package com.example.image.service;

import com.example.image.entity.Image;
import com.example.image.repository.ImageRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;

import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class ImageServiceTest {

    @Mock
    private ImageRepository imageRepository;

    @InjectMocks
    private ImageService imageService;

    @Value("${app.file.upload-dir}")
    private String storagePath;

    public ImageServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllImages() {
        when(imageRepository.findAll()).thenReturn(Collections.emptyList());

        List<Image> images = imageService.getAllImages();

        assertNotNull(images);
        assertTrue(images.isEmpty());
        verify(imageRepository, times(1)).findAll();
    }

    @Test
    void testGetAllImagesByPage() {
        String page = "somePage";
        when(imageRepository.findAllByPage(page)).thenReturn(Collections.emptyList());

        List<Image> images = imageService.getAllImagesByPage(page);

        assertNotNull(images);
        assertTrue(images.isEmpty());
        verify(imageRepository, times(1)).findAllByPage(page);
    }

    @Test
    void testGetImageById() {
        Long id = 1L;
        Image image = new Image();
        when(imageRepository.findById(id)).thenReturn(Optional.of(image));

        Image retrievedImage = imageService.getImageById(id);

        assertNotNull(retrievedImage);
        verify(imageRepository, times(1)).findById(id);
    }

    @Test
    void testAddImage() {
        ImageService imageService = new ImageService(imageRepository,"D:/Users/Admin/Documents/projetStage/Backend/sesam/Image/static/upload-dir");

        MultipartFile file = new MockMultipartFile("file", "test-image.png", "image/png", "content".getBytes());
        when(imageRepository.save(any())).thenReturn(new Image());

        Image addedImage = imageService.addImage(file, 1L, "somePage");

        assertNotNull(addedImage);
        verify(imageRepository, times(1)).save(any());
    }

    @Test
    void testDeleteImage() {
        Long id = 1L;
        Image image = new Image(1L,"filename","image",1L,"page",1L);
        when(imageRepository.findById(id)).thenReturn(Optional.of(image));
        boolean isDeleted = imageService.deleteImage(id);

        assertTrue(isDeleted);
        verify(imageRepository, times(1)).findById(id);
        verify(imageRepository, times(1)).delete(image);
    }

    @Test
    void testUpdateImage() {
        Long id = 1L;
        Image image = new Image();
        when(imageRepository.findById(id)).thenReturn(Optional.of(image));
        when(imageRepository.save(any())).thenReturn(new Image());

        MultipartFile file = new MockMultipartFile("file", "test-image.png", "image/png", "content".getBytes());

        Image updatedImage = imageService.updateImage(id, file);

        assertNotNull(updatedImage);
        verify(imageRepository, times(1)).findById(id);
        verify(imageRepository, times(1)).save(any());
    }
}
