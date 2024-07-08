package com.bma.CloudFileStorage.controllers;

import com.bma.CloudFileStorage.models.dto.ObjectRequestDto;
import com.bma.CloudFileStorage.services.MinioService;
import com.bma.CloudFileStorage.util.CastDtoUtil;
import io.minio.GetObjectResponse;
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

    @PostMapping()
    public RedirectView uploadFile(@RequestParam("file") MultipartFile file,
                                   @RequestParam(value = "path", required = false, defaultValue = "") String path) {

        try {
            minioService.uploadFile(file, path);
        } catch (Exception e) {
            throw new RuntimeException("Error occurred: " + e.getMessage());
        }
        RedirectView redirectView = new RedirectView();

        redirectView.setUrl("/?path=" +  URLEncoder.encode(path));
        return redirectView;
    }

    @GetMapping(produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<InputStreamResource> downloadFile(@ModelAttribute ObjectRequestDto downloadFileRequestDto) {



        GetObjectResponse getObjectResponse = minioService.downloadFile(downloadFileRequestDto);
        String encodedFileName = URLEncoder.encode(downloadFileRequestDto.getName());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + encodedFileName)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
                .body(new InputStreamResource(getObjectResponse));


    }

    @DeleteMapping()
    public String deleteFile(@ModelAttribute ObjectRequestDto deleteFileRequestDto) {

        String redirectPath ="";

        if (deleteFileRequestDto.getPath().contains("/")){
            redirectPath = deleteFileRequestDto.getPath().substring(0, deleteFileRequestDto.getPath().lastIndexOf("/"));
        }

        minioService.deleteFile(deleteFileRequestDto);

        return "redirect:/?path=" + redirectPath;
    }
    @PatchMapping()
    public String renameFile(@ModelAttribute ObjectRequestDto renameFileDto){
        String redirectPath ="";

        if (renameFileDto.getPath().contains("/")){
            redirectPath = renameFileDto.getPath().substring(0, renameFileDto.getPath().lastIndexOf("/"));
        }
       minioService.renameFile(CastDtoUtil.castToRenameFileDto(renameFileDto));



        return "redirect:/?path=" + redirectPath;
    }

}
