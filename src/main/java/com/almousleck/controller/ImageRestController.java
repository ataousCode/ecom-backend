package com.almousleck.controller;

import com.almousleck.dto.ImageDto;
import com.almousleck.exceptions.ResourceNotFound;
import com.almousleck.model.Image;
import com.almousleck.response.ApiResponse;
import com.almousleck.service.ImageService;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("${api.prefix}/images")
@RequiredArgsConstructor
public class ImageRestController {
    private final ImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse> saveImages(@RequestParam List<MultipartFile> files, @RequestParam Long productId) {
        try {
            List<ImageDto> imageDtoList = imageService.saveImage(productId, files);
            return ResponseEntity.ok(new ApiResponse("Uploaded successfully", imageDtoList));
        }catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Ops! something went wrong while updating", e.getMessage()));
        }
    }

    //todo: /api/v1/images/image/download/
    @GetMapping("/image/download/{imageId}")
    @Transactional
    public ResponseEntity<Resource> downloadImage(@PathVariable Long imageId) throws SQLException {
        Image image = imageService.getImageById(imageId);
//        ByteArrayResource resource = new ByteArrayResource(image.getImage()
//                .getBytes(1, (int) image.getImage().length()));
//        return ResponseEntity.ok().contentType(MediaType.parseMediaType(image.getFileType()))
//                .header(HttpHeaders.CONTENT_DISPOSITION,
//                        "attachment; filename=\"" + image.getFileName() + "\"")
//                .body(resource);
        try (InputStream inputStream = image.getImage().getBinaryStream()) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            ByteArrayResource resource = new ByteArrayResource(outputStream.toByteArray());

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(image.getFileType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + image.getFileName() + "\"")
                    .body(resource);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/update/{imageId}")
    public ResponseEntity<ApiResponse> updateImage(@PathVariable Long imageId, @RequestBody MultipartFile file) {
        try {
            Image image = imageService.getImageById(imageId);
            if (image != null) {
                imageService.updateImage(file, imageId);
                return ResponseEntity.ok(new ApiResponse("Update successfully", null));
            }
        }catch (ResourceNotFound ex) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse(ex.getMessage(), null));
        }
        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .body(new ApiResponse("Ops! something went wrong while updating", INTERNAL_SERVER_ERROR));
    }

    @DeleteMapping("/delete/{imageId}")
    public ResponseEntity<ApiResponse> deleteImage(@PathVariable Long imageId) {
        try {
            Image image = imageService.getImageById(imageId);
            if (image != null) {
                imageService.deleteImageById(imageId);
                return ResponseEntity.ok(new ApiResponse("Delete success!", null));
            }
        }catch (ResourceNotFound ex) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse(ex.getMessage(), null));
        }
        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .body(new ApiResponse("Ops! something went wrong while deleting", INTERNAL_SERVER_ERROR));
    }
















}
