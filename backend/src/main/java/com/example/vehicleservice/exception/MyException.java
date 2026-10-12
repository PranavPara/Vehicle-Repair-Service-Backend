package com.example.vehicleservice.exception;

// We throw this when something is wrong, for example:
// throw new MyException("Vehicle not found");
public class MyException extends RuntimeException {

    public MyException(String message) {
        super(message);
    }
}
