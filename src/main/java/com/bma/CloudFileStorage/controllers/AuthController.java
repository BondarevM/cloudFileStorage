package com.bma.CloudFileStorage.controllers;

import com.bma.CloudFileStorage.models.dto.CustomerDto;
import com.bma.CloudFileStorage.repositories.CustomerRepository;
import com.bma.CloudFileStorage.services.RegistrationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController extends AbstractController {
    private final RegistrationService registrationService;
    private final CustomerRepository customerRepository;

    @Autowired
    public AuthController(RegistrationService registrationService, CustomerRepository customerRepository) {
        this.registrationService = registrationService;
        this.customerRepository = customerRepository;
    }

    @GetMapping("/login")
    public String login() {
        return "/login";
    }

    @GetMapping("/registration")
    public String registration(@ModelAttribute("customerDto") CustomerDto customerDto) {
        return "/registration";
    }

    @PostMapping("/registration")
    public String performRegistration(@ModelAttribute("customerDto") @Valid CustomerDto customerDto,
                                      BindingResult bindingResult) {

        registrationService.register(customerDto, bindingResult);

        if (bindingResult.hasErrors()) {
            return "/registration";
        }

        return "redirect:/login";
    }

}
