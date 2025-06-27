package com.ceres.project.utils;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class NotAcceptable406 extends Exception {
    public NotAcceptable406(String message) {
        super(message);
    }
    public NotAcceptable406(String message, Throwable cause) {
        super(message, cause);
    }
}