package com.ceres.project.utils;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFound404 extends Exception{
    public NotFound404(String message) {
        super(message);
    }
    public NotFound404(String message, Throwable cause) {
        super(message, cause);
    }
}
