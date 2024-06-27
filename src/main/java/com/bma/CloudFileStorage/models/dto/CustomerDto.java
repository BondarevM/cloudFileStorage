package com.bma.CloudFileStorage.models.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerDto {

    private Integer id;

    private String login;

    private String password;

    private String confirmedPassword;
}
