package com.example.marketour.services;

import com.example.marketour.model.entities.Image;
import com.example.marketour.repositories.ImageRepository;
import org.springframework.stereotype.Service;

@Service
public class ImageService {

    private final ImageRepository imageRepository;

    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public Image addImage(String description, byte[] data) {
        final var image = new Image();
        image.setData(data);
        image.setDescription(description);
        return imageRepository.save(image);
    }


    public Image getImageByTourId(Long tourId) {
        return imageRepository.findAll().stream().filter(image -> image.getImageId().equals(tourId)).findFirst().orElse(null);
    }

    public void updateImage(Image image) {
        imageRepository.save(image);
    }
}
