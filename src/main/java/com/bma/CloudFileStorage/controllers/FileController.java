package com.bma.CloudFileStorage.controllers;

import com.bma.CloudFileStorage.services.MinioService;
import io.minio.errors.MinioException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

@Controller
public class FileController {
    private final MinioService minioService;

    public FileController(MinioService minioService) {
        this.minioService = minioService;
    }

    @PostMapping("/uploadFile")
    public String uploadFile(@RequestParam("file") MultipartFile file) {

        try {
            minioService.uploadFile(file.getOriginalFilename(), file.getContentType(), file.getInputStream());
        } catch (Exception e ){
            throw new RuntimeException("Error occurred: " + e.getMessage());
        }

        return "redirect:/";
    }

    @PostMapping("/uploadFolder")
    public String uploadFolder(@RequestParam("folder") MultipartFile[] folder){
       int i =1;
       int qi =1;

        List<MultipartFile> list = Arrays.stream(folder).toList();
        System.out.println();

        for (MultipartFile multipartFile : list){
            try {
                minioService.uploadFile(multipartFile.getOriginalFilename(),multipartFile.getContentType(),multipartFile.getInputStream());
            } catch (MinioException | NoSuchAlgorithmException | InvalidKeyException | IOException e) {
                throw new RuntimeException(e);
            }
        }


//        minioService.uploadFolder(multipartFile);
        return "redirect:/";
    }





}
