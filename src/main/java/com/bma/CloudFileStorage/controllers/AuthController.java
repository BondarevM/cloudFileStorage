package com.bma.CloudFileStorage.controllers;

import com.bma.CloudFileStorage.models.dto.CustomerDto;
import com.bma.CloudFileStorage.services.RegistrationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {
    private final RegistrationService registrationService;

    @Autowired
    public AuthController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @GetMapping("/login")
    public String login (){
        return "/login";
    }

    @GetMapping("/registration")
    public String registration (@ModelAttribute("customerDto") CustomerDto customerDto){
        return "/registration";
    }

    @PostMapping("/registration")
    public String performRegistration(@ModelAttribute("customerDto") @Valid CustomerDto customerDto,
                                      BindingResult bindingResult){

        registrationService.register(customerDto);
        return "redirect:/login";
    }


}
