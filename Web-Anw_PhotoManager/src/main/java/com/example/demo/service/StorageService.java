package com.example.demo.service;

import com.example.demo.entity.ImageData;
import com.example.demo.payload.response.MessageResponse;
import com.example.demo.repository.StorageRepository;
import com.example.demo.utils.ImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class StorageService {

    private static final String FILE_NAME_DUPLICATED = "101";

    private static final String FILE_UPLOAD_FAIL = "102";

    private static final String FILE_NAME_SUCCESS = "200";

    @Autowired
    private StorageRepository storageRepository;

    public ResponseEntity<?> uploadImage(MultipartFile file) throws IOException {
        Optional<ImageData> imageDataInDb = storageRepository.findByName(file.getOriginalFilename());
        if (imageDataInDb.isPresent())
            return new ResponseEntity<>(new MessageResponse(FILE_NAME_DUPLICATED, "File Name is duplicated"),
                  HttpStatus.BAD_REQUEST);

        ImageData imageData = storageRepository.save(ImageData.builder()
              .name(file.getOriginalFilename())
              .type(file.getContentType())
              .imageData(ImageUtils.compressImage(file.getBytes())).build());
        if (imageData != null) {
            return ResponseEntity.ok(new MessageResponse(FILE_NAME_SUCCESS,
                  "File uploaded successfully " + file.getOriginalFilename()));
        }
        return new ResponseEntity<>(new MessageResponse(FILE_UPLOAD_FAIL, "File upload fail"),
              HttpStatus.BAD_REQUEST);
    }

    public byte[] downloadImage(String fileName) {
        Optional<ImageData> dbImageData = storageRepository.findByName(fileName);
        byte[] images = ImageUtils.decompressImage(dbImageData.get().getImageData());
        return images;
    }
}
