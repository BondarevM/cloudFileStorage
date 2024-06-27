package com.bma.CloudFileStorage.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController extends AbstractController {

    @GetMapping("/")
    public String hello(Model model){
        return "index";
    }

}
