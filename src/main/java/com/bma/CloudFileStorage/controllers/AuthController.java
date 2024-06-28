package com.bma.CloudFileStorage.controllers;

import com.bma.CloudFileStorage.exceptions.PasswordMismatchException;
import com.bma.CloudFileStorage.exceptions.UserAlreadyExistsException;
import com.bma.CloudFileStorage.models.dto.CustomerDto;
import com.bma.CloudFileStorage.repositories.CustomerRepository;
import com.bma.CloudFileStorage.services.RegistrationService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.password.CompromisedPasswordException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController extends AbstractController {
    private final RegistrationService registrationService;


    @Autowired
    public AuthController(RegistrationService registrationService, CustomerRepository customerRepository) {
        this.registrationService = registrationService;
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
                                      BindingResult bindingResult,
                                      RedirectAttributes redirectAttributes) {

            registrationService.validateCustomer(customerDto);

        if (bindingResult.hasErrors()) {
            return "/registration";
        }

        registrationService.register(customerDto);

        redirectAttributes.getFlashAttributes();

        return "redirect:/login";
    }

}
