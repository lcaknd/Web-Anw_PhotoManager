package com.example.demo.exception;

import com.example.demo.payload.response.MessageResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.naming.AuthenticationException;

@RestControllerAdvice
public class GlobalExceptionHandler{

//    @ExceptionHandler(UsernameNotFoundException)
//    @ResponseStatus(HttpStatus.SC_UNAUTHORIZED)
//    @ResponseBody MessageResponse
//    UnauthorizeExceptionInfo(HttpServletRequest req, Exception ex) {
//        return new MessageResponse(ex.getMessage());
//    }
}
