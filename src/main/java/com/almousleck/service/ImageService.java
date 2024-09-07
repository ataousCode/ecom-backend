package com.almousleck.service;

import com.almousleck.dto.ImageDto;
import com.almousleck.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService {
    Image getImageById(Long id);
    void deleteImageById(Long id);
    List<ImageDto> saveImage(List<MultipartFile> files, Long productId);
    void  updateImage(MultipartFile  file, Long imageId);
}
