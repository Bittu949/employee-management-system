//package com.company.ems.Exception;
//
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//
//@ControllerAdvice
//public class GlobalExceptionHandler {
//
//    @ExceptionHandler(ResourceNotFoundException.class)
//    public String handleNotFound(ResourceNotFoundException ex, Model model){
//        model.addAttribute("errorMessage", ex.getMessage());
//        return "error";
//    }
//
//    @ExceptionHandler(DuplicateResourceException.class)
//    public String handleDuplicate(DuplicateResourceException ex, Model model){
//        model.addAttribute("errorMessage", ex.getMessage());
//        return "error";
//    }
//
//    @ExceptionHandler(BadRequestException.class)
//    public String handleBadRequest(BadRequestException ex, Model model){
//        model.addAttribute("errorMessage", ex.getMessage());
//        return "error";
//    }
//
//    @ExceptionHandler(IllegalOperationException.class)
//    public String handleIllegalOperation(IllegalOperationException ex, Model model){
//        model.addAttribute("errorMessage", ex.getMessage());
//        return "error";
//    }
//
//    @ExceptionHandler(UnauthorizedException.class)
//    public String handleUnauthorized(UnauthorizedException ex, Model model){
//        model.addAttribute("errorMessage", "You are not authorized!");
//        return "error";
//    }
//
//    @ExceptionHandler(ForbiddenException.class)
//    public String handleForbidden(ForbiddenException ex, Model model){
//        model.addAttribute("errorMessage", "Access denied!");
//        return "error";
//    }
//
//    @ExceptionHandler(Exception.class)
//    public String handleGeneral(Exception ex, Model model){
//        model.addAttribute("errorMessage", "Something went wrong!");
//        return "error";
//    }
//}




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