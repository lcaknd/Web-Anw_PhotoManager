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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StorageService {

    private static final String FILE_NAME_DUPLICATED = "101";

    private static final String FILE_UPLOAD_FAIL = "102";

    private static final String SUCCESS = "200";

    @Autowired
    private StorageRepository storageRepository;

    public ResponseEntity<MessageResponse> uploadImage(MultipartFile file) throws IOException {
        Optional<ImageData> imageDataInDb = storageRepository.findByName(file.getOriginalFilename());
        if (imageDataInDb.isPresent())
            return new ResponseEntity<>(new MessageResponse(FILE_NAME_DUPLICATED, "File Name is duplicated"),
                  HttpStatus.BAD_REQUEST);

        ImageData imageData = storageRepository.save(ImageData.builder()
              .name(file.getOriginalFilename())
              .type(file.getContentType())
              .imageData(ImageUtils.compressImage(file.getBytes())).build());
        if (imageData != null) {
            return ResponseEntity.ok(new MessageResponse(SUCCESS,
                  null, "File uploaded successfully " + file.getOriginalFilename()));
        }
        return new ResponseEntity<>(new MessageResponse(FILE_UPLOAD_FAIL, "File upload fail"),
              HttpStatus.BAD_REQUEST);
    }

    public byte[] downloadImage(String fileName) {
        ImageData dbImageData = storageRepository.findByName(fileName).orElseThrow(() ->
              new RuntimeException("ERROR WHEN DOWNLOADING"));
        return ImageUtils.decompressImage(dbImageData.getImageData());
    }

    public ResponseEntity<MessageResponse> searchByFileName(String fileName) {
        List<ImageData> imageDataList = storageRepository.findAllByNameLike(fileName);
        List<String> imageNameList = imageDataList.stream()
              .map(item -> item.getName())
              .collect(Collectors.toList());
        return ResponseEntity.ok(new MessageResponse(SUCCESS, imageNameList, "SUCCESS"));
    }
}
