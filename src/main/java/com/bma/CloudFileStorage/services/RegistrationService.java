package com.bma.CloudFileStorage.services;

import com.bma.CloudFileStorage.exceptions.PasswordMismatchException;
import com.bma.CloudFileStorage.exceptions.UserAlreadyExistsException;
import com.bma.CloudFileStorage.models.Customer;
import com.bma.CloudFileStorage.models.dto.CustomerDto;
import com.bma.CloudFileStorage.repositories.CustomerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.password.CompromisedPasswordException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Service
public class RegistrationService {
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;


    @Autowired
    public RegistrationService(CustomerRepository customerRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public void register(CustomerDto customerDto) {


        Customer customer = modelMapper.map(customerDto, Customer.class);

        customer.setPassword(passwordEncoder.encode(customer.getPassword()));


        customerRepository.save(customer);

    }


    public void validateCustomer(CustomerDto customerDto) {
        if (!customerDto.getPassword().equals(customerDto.getConfirmedPassword())) {
            throw  new PasswordMismatchException("Password mismatch");
        }

        if (customerRepository.findByLogin(customerDto.getLogin()).isPresent()) {
            throw new UserAlreadyExistsException("User with this login already exists");
        }
    }
}
