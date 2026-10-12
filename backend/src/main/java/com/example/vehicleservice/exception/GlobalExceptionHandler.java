package com.example.vehicleservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

// Catches every error in the project and sends back a simple message like {"message": "Vehicle not found"}
// The website shows this message to the user.
@RestControllerAdvice
public class GlobalExceptionHandler {

    // our own errors (MyException)
    @ExceptionHandler(MyException.class)
    public ResponseEntity<Map<String, String>> handleMyException(MyException e) {
        Map<String, String> body = new HashMap<>();
        body.put("message", e.getMessage());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    // errors from the validation annotations (@NotBlank, @Min ...)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        Map<String, String> body = new HashMap<>();
        body.put("message", message);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    // any other error
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleOther(Exception e) {
        e.printStackTrace();
        Map<String, String> body = new HashMap<>();
        body.put("message", "Something went wrong on the server");
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
