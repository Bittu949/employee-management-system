package com.company.ems.Exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(EMSException.class)
    public String handleEMSException(EMSException ex, Model model){
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("statusCode", ex.getStatusCode());
        return "error";
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneral(Exception ex, Model model){
        ex.printStackTrace();

        model.addAttribute("errorMessage", "Unexpected error occurred!");
        model.addAttribute("statusCode", 500);
        return "error";
    }
}