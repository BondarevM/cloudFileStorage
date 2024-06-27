package com.bma.CloudFileStorage.services;

import com.bma.CloudFileStorage.models.Customer;
import com.bma.CloudFileStorage.models.dto.CustomerDto;
import com.bma.CloudFileStorage.repositories.CustomerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

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

    public void register(CustomerDto customerDto, BindingResult bindingResult){
        validateCustomer(customerDto,  bindingResult);

        if (bindingResult.hasErrors())
            return;

        Customer customer = modelMapper.map(customerDto, Customer.class);

        customer.setPassword(passwordEncoder.encode(customer.getPassword()));

        customerRepository.save(customer);
    }


    private void validateCustomer(CustomerDto customerDto,
                                  BindingResult bindingResult) {
        if (!customerDto.getPassword().equals(customerDto.getConfirmedPassword())) {
            bindingResult.rejectValue("confirmedPassword", "400", "Password mismatch");
        }

        if (customerRepository.findByLogin(customerDto.getLogin()).isPresent()){
            bindingResult.rejectValue("login", "400", "User with this login already exist");
        }
    }
}
