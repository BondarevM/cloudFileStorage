package com.bma.CloudFileStorage.controllers;

import com.bma.CloudFileStorage.exceptions.IllegalFileNameException;
import com.bma.CloudFileStorage.exceptions.IllegalFolderNameException;
import com.bma.CloudFileStorage.exceptions.PasswordMismatchException;
import com.bma.CloudFileStorage.exceptions.UserAlreadyExistsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.net.http.HttpClient;

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

    @ExceptionHandler(IllegalFileNameException.class)
    public RedirectView illegalFileNameExceptionHandle(IllegalFileNameException e, RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("error", e.getMessage());
        return new RedirectView("/", true);
    }

    @ExceptionHandler(IllegalFolderNameException.class)
    public RedirectView illegalFolderNameExceptionHandle(IllegalFolderNameException e, RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("error", e.getMessage());
        return new RedirectView("/", true);
    }
}
