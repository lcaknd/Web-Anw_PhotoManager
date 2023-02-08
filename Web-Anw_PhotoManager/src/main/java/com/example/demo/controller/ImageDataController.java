package com.example.demo.controller;

import com.example.demo.payload.response.MessageResponse;
import com.example.demo.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/image")
public class ImageDataController {

    @Autowired
    private StorageService service;

    @PostMapping("")
    public ResponseEntity<MessageResponse> uploadImage(@RequestParam("image") MultipartFile file) throws IOException {
        return service.uploadImage(file);
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<?> downloadImage(@PathVariable("fileName") String fileName) {
        byte[] imageData = service.downloadImage(fileName);
        return ResponseEntity.status(HttpStatus.OK)
              .contentType(MediaType.valueOf("image/png"))
              .body(imageData);
    }

    @GetMapping("/search/{fileName}")
    public ResponseEntity<MessageResponse> searchByFileName(@PathVariable("fileName") String fileName) {
        return service.searchByFileName(fileName);
    }
}
