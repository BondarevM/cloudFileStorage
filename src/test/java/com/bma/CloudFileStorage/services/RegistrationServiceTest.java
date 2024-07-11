package com.bma.CloudFileStorage.services;

import com.bma.CloudFileStorage.exceptions.UserAlreadyExistsException;
import com.bma.CloudFileStorage.models.Customer;
import com.bma.CloudFileStorage.models.dto.CustomerDto;
import com.bma.CloudFileStorage.repositories.CustomerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

@Testcontainers
@SpringBootTest
class RegistrationServiceTest {

    @Autowired
    private RegistrationService registrationService;
    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void callingRegistrationMethodCreatesUserInDatabase() {
        String login = "login";
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

    @Test
    public void savingUserWithNonUniqueLoginThrowsException() {
        String login = "login2";
        String password = "password";

        CustomerDto customerDto = new CustomerDto();
        customerDto.setLogin(login);
        customerDto.setPassword(password);
        customerDto.setConfirmedPassword(password);

        registrationService.validateCustomer(customerDto);
        registrationService.register(customerDto);

        String login2 = "login2";
        String password2 = "password2";
        CustomerDto customerDto2 = new CustomerDto();
        customerDto2.setLogin(login2);
        customerDto2.setPassword(password2);
        customerDto2.setConfirmedPassword(password2);
        Assertions.assertThrows(UserAlreadyExistsException.class, () -> registrationService.validateCustomer(customerDto2));
    }


}