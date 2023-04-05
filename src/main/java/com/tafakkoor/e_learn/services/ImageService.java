package com.tafakkoor.e_learn.services;

import com.tafakkoor.e_learn.domain.Image;
import com.tafakkoor.e_learn.repository.ImageRepository;
import org.springframework.stereotype.Service;

@Service
public class ImageService {
    private final ImageRepository imageRepository;

    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public Image saveImage(Image image) {
        return imageRepository.save(image);
    }
    public Image findById(Long id){
        return imageRepository.findById(id).orElse(null);
    }
}
