package com.bma.CloudFileStorage.controllers;

import com.bma.CloudFileStorage.models.dto.DownloadFileRequestDto;
import com.bma.CloudFileStorage.services.MinioService;
import io.minio.errors.MinioException;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
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
    public String uploadFile(@RequestParam("file") MultipartFile file,
                             @RequestParam(value = "path", required = false, defaultValue = "") String path) {

        int length = path.length();

        try {
            minioService.uploadFile(file);
        } catch (Exception e) {
            throw new RuntimeException("Error occurred: " + e.getMessage());
        }

        return "redirect:/";
    }

    @GetMapping(value = "/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<InputStreamResource> downloadFile(@ModelAttribute DownloadFileRequestDto downloadFileRequestDto) {
        InputStreamResource file = minioService.download(downloadFileRequestDto).block();
        String encodedFileName = URLEncoder.encode(downloadFileRequestDto.getName());


        System.out.println();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + encodedFileName)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE).body(file);


    }
//        InputStreamResource inputStreamResource = minioService.downloadFile(downloadFileRequestDto);
//
//        String fileName = downloadFileRequestDto.getOwner() + "/" + downloadFileRequestDto.getPath();
//
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
//                .contentType(MediaType.APPLICATION_OCTET_STREAM)
//                .body(new InputStreamResource(inputStreamResource));
//    }

}
