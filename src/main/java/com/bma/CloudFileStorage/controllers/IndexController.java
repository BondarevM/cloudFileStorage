package com.bma.CloudFileStorage.controllers;

import com.bma.CloudFileStorage.models.dto.CreateEmptyFolderDto;
import com.bma.CloudFileStorage.models.dto.MinioResponseObjectDto;
import com.bma.CloudFileStorage.services.MinioService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class IndexController extends AbstractController {
    private  final MinioService minioService;

    public IndexController(MinioService minioService) {
        this.minioService = minioService;
    }

    @GetMapping("/")
    public String homePage(Model model,
                           @RequestParam(value = "path", required = false, defaultValue = "") String path) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();List<MinioResponseObjectDto> objects = minioService.getFiles(authentication.getName(), path);
//
//        List<String> files = objects.get("fileNames");
//        List<String> folders = objects.get("folderNames");
//
//        System.out.println();
//
//
//        model.addAttribute("files", files);
//        model.addAttribute("folders", folders);
        model.addAttribute("createEmptyFolderDto", new CreateEmptyFolderDto());
        model.addAttribute("objects", objects);

        return "index";
    }

}
