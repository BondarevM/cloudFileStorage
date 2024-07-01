package com.bma.CloudFileStorage.controllers;

import com.bma.CloudFileStorage.models.dto.MinioResponseObjectDto;
import com.bma.CloudFileStorage.services.MinioService;
import io.minio.errors.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

@Controller
public class IndexController extends AbstractController {
    private  final MinioService minioService;

    public IndexController(MinioService minioService) {
        this.minioService = minioService;
    }

    @GetMapping("/")
    public String homePage(Model model,
                           @RequestParam(value = "path", required = false, defaultValue = "") String path) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        List<MinioResponseObjectDto> objects = minioService.getFiles(authentication.getName(), path);
//
//        List<String> files = objects.get("fileNames");
//        List<String> folders = objects.get("folderNames");
//
//        System.out.println();
//
//
//        model.addAttribute("files", files);
//        model.addAttribute("folders", folders);
        model.addAttribute("objects", objects);
        return "index";
    }

}
