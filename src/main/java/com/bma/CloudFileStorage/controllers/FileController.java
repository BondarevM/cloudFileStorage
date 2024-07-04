package com.bma.CloudFileStorage.controllers;

import com.bma.CloudFileStorage.models.dto.DownloadFileRequestDto;
import com.bma.CloudFileStorage.services.MinioService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import java.net.URLEncoder;

@Controller
@RequestMapping("/file")
public class FileController {
    private final MinioService minioService;

    public FileController(MinioService minioService) {
        this.minioService = minioService;
    }

    @PostMapping("/upload")
    public RedirectView uploadFile(@RequestParam("file") MultipartFile file,
                             @RequestParam(value = "path", required = false, defaultValue = "") String path) {

        try {
            minioService.uploadFile(file, path);
        } catch (Exception e) {
            throw new RuntimeException("Error occurred: " + e.getMessage());
        }
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("/?path=" + path);
        return redirectView;
    }

    @GetMapping(value = "/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<InputStreamResource> downloadFile(@ModelAttribute DownloadFileRequestDto downloadFileRequestDto) {
        InputStreamResource file = minioService.downloadFile(downloadFileRequestDto).block();
        String encodedFileName = URLEncoder.encode(downloadFileRequestDto.getName());


        System.out.println();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + encodedFileName)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE).body(file);


    }
}
