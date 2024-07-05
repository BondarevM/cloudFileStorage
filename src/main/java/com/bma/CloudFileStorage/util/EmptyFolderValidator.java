package com.bma.CloudFileStorage.util;

import com.bma.CloudFileStorage.models.dto.CreateEmptyFolderDto;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
@Component
public class CreateFolderRequestDtoValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return CreateEmptyFolderDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
    CreateEmptyFolderDto dto = (CreateEmptyFolderDto) target;
    if (dto.getName().contains("/")){
        errors.rejectValue("error", HttpStatus.BAD_REQUEST.toString(), "The folder must not contain '/' in name");
    }
    }
}
