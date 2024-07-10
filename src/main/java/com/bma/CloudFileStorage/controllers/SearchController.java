package com.bma.CloudFileStorage.controllers;

import com.bma.CloudFileStorage.models.dto.CreateEmptyFolderDto;
import com.bma.CloudFileStorage.models.dto.MinioResponseObjectDto;
import com.bma.CloudFileStorage.models.dto.SearchRequestDto;
import com.bma.CloudFileStorage.services.SearchService;
import jakarta.validation.Valid;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/search")

public class SearchController extends AbstractController {
    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping()
    public String search(@RequestParam String query,
                         Model model){
        String owner = SecurityContextHolder.getContext().getAuthentication().getName();

        List<MinioResponseObjectDto> searchResult = searchService.searchObjects(owner, query);
        model.addAttribute("searchResult", searchResult);
        model.addAttribute("query", query);

        return "search";
    }
}
