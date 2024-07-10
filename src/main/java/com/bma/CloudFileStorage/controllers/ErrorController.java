package com.bma.CloudFileStorage.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController extends AbstractController{

    @GetMapping("/error")
    public String error(Model model){
        return "/error";
    }

}
