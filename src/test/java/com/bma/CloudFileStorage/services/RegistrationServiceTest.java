package com.bma.CloudFileStorage.services;

import com.bma.CloudFileStorage.models.Customer;
import com.bma.CloudFileStorage.models.dto.CustomerDto;
import com.bma.CloudFileStorage.repositories.CustomerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@Testcontainers
@SpringBootTest

class RegistrationServiceTest {

    @Autowired
    private  RegistrationService registrationService;
    @Autowired
    private CustomerRepository customerRepository;

//    @Container
//    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16.3");
//
//    @DynamicPropertySource
//    static void configureProperties(DynamicPropertyRegistry registry){
//        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
//        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
//        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
//        registry.add("spring.jpa.generate-ddl", () -> true);
//    }

    @Test
    public void callingRegistrationMethodCreatesUserInDatabase(){
        String login = "login228";
        String password = "password";


        CustomerDto customerDto = new CustomerDto();
        customerDto.setLogin(login);
        customerDto.setPassword(password);
        customerDto.setConfirmedPassword(password);

        registrationService.validateCustomer(customerDto);
        registrationService.register(customerDto);

        Optional<Customer> customer = customerRepository.findByLogin(login);
        Assertions.assertTrue(customer.isPresent());


    }


}