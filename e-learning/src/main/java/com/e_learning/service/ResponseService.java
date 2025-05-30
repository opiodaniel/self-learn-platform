package com.e_learning.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class ResponseService {

    public ResponseEntity<Map<String, Object>> createSuccessResponse(int returnCode, Object returnObject, HttpStatus status) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", returnCode);
        response.put("result", returnObject);
        return new ResponseEntity<>(response, status);
    }

    public ResponseEntity<Map<String, Object>> createErrorResponse(int returnCode, Map<String, String> errorMessages, HttpStatus status) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", returnCode);
        response.put("errors", errorMessages);
        return new ResponseEntity<>(response, status);
    }
}

