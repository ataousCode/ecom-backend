package com.almousleck.service;

import com.almousleck.dto.ImageDto;
import com.almousleck.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService {
    Image getImageById(Long id);
    void deleteImageById(Long id);
    List<ImageDto> saveImage(Long productId, List<MultipartFile> files);
    void  updateImage(MultipartFile  file, Long imageId);
}
