package com.bma.CloudFileStorage.controllers;

import com.bma.CloudFileStorage.services.MinioService;
import io.minio.errors.MinioException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/folder")
public class FolderController {
    private final MinioService minioService;

    public FolderController(MinioService minioService) {
        this.minioService = minioService;
    }

    @PostMapping("/upload")
    public RedirectView uploadFolder(@RequestParam("folder") MultipartFile[] folder,
                               @RequestParam(value = "path", defaultValue = "", required = false) String path){
        List<MultipartFile> list = Arrays.stream(folder).toList();
        try {
            minioService.uploadFolder(list, path);
        } catch (MinioException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Error occurred: " + e.getMessage());
        }
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("/?path=" + path);
        return redirectView;
    }
}
