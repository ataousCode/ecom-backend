package com.almousleck.service.image;

import com.almousleck.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService {
    Image getImageById(Long id);
    void deleteImageById(Long id);
    Image saveImage(List<MultipartFile> files, Long productId);
    void  updateImage(MultipartFile  file, Long imageId);
}
