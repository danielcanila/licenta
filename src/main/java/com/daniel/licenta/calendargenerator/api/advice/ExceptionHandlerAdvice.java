package com.daniel.licenta.calendargenerator.api.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class ExceptionHandlerAdvice extends ResponseEntityExceptionHandler {

//    @ExceptionHandler(RuntimeException.class)
//    public ResponseEntity<String> handleUserNotFoundException(Exception ex, WebRequest request) {
//        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
//    }

}
