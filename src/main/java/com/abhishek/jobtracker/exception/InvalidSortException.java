package com.abhishek.jobtracker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidSortException extends RuntimeException {

    public InvalidSortException(String message) {
        super(message);
    }
}