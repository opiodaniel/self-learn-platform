package com.ceres.project.utils;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequest400 extends Exception{
    public BadRequest400(String message) {
        super(message);
    }
    public BadRequest400(String message, Throwable cause) {
        super(message, cause);
    }
}
