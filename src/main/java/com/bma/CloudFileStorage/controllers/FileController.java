package com.bma.CloudFileStorage.controllers;

import com.bma.CloudFileStorage.services.MinioService;
import io.minio.errors.MinioException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Controller
public class FileController {
    private final MinioService minioService;

    public FileController(MinioService minioService) {
        this.minioService = minioService;
    }

    @PostMapping("/uploadFile")
    public String uploadFile(@RequestParam("file") MultipartFile multipartFile) {

        try {
            minioService.uploadFile(multipartFile.getOriginalFilename(), multipartFile.getContentType(), multipartFile.getInputStream());
        } catch (Exception e ){
            throw new RuntimeException("Error occurred: " + e.getMessage());
        }

        return "redirect:/";
    }

    @PostMapping("/uploadFolder")
    public String uploadFolder(@RequestParam("folder") MultipartFile[] multipartFile){




//        minioService.uploadFolder(multipartFile);
        return "redirect:/";
    }





}
