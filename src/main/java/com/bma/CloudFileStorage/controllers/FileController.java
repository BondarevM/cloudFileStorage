package com.bma.CloudFileStorage.controllers;

import com.bma.CloudFileStorage.services.MinioService;
import io.minio.errors.MinioException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/file")
public class FileController {
    private final MinioService minioService;

    public FileController(MinioService minioService) {
        this.minioService = minioService;
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) {

        try {
            minioService.uploadFile(file);
        } catch (Exception e ){
            throw new RuntimeException("Error occurred: " + e.getMessage());
        }

        return "redirect:/";
    }

}
