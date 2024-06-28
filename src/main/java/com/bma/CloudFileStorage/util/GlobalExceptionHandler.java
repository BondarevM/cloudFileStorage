package com.bma.CloudFileStorage.util;

import com.bma.CloudFileStorage.exceptions.PasswordMismatchException;
import com.bma.CloudFileStorage.exceptions.UserAlreadyExistsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserAlreadyExistsException.class)
    public RedirectView userAlreadyExistExceptionHandle(UserAlreadyExistsException e, RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("error", e.getMessage());
        return new RedirectView("/registration", true);
    }

    @ExceptionHandler(PasswordMismatchException.class)
    public RedirectView passwordMismatchExceptionHandle(PasswordMismatchException e, RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("passwordMismatchError", e.getMessage());
        return new RedirectView("/registration", true);
    }
}
