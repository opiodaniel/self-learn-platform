package com.ceres.project.utils;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class MasterResponse {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp = new Timestamp(System.currentTimeMillis()).toLocalDateTime();
    ServletUriComponentsBuilder request = ServletUriComponentsBuilder.fromCurrentRequest();
    private String path = request.toUriString();
    private String message;

    public Map<String, Object> bodyGen(String message, Integer status){
        Map<String, Object> res = new HashMap<>();
        res.put("status", status);
        res.put("timestamp", timestamp);
        res.put("message", message);
        res.put("path", path);
        return  res;
    }

    /**
     * @apiNote The data requested for was not found
     * @param message The message to return along the response(required)
     * */
    public ResponseEntity<Map<String, Object>> ResponseError404(String message){
        Map<String, Object> res= bodyGen(message, 404);
        return ResponseEntity.status(404).body(res);
    }

    /**
     * @apiNote User has no permission to perform this action
     * @param message The message to return along the response(required)
     * */
    public ResponseEntity<Map<String, Object>> ResponseError403(String message){
        Map<String, Object> res= bodyGen(message, 403);
        return ResponseEntity.status(403).body(res);
    }

    /**
     * @apiNote Bad request, e.g. user sent wrong data, request missing some important info, an exception was raised
     * while processing the request, all these make the request go bad.
     * @param message The message to return along the response(required)
     * */
    public ResponseEntity<Map<String, Object>> ResponseError400(String message){
        Map<String, Object> res= bodyGen(message, 400);
        return ResponseEntity.status(400).body(res);
    }

    /**
     * @apiNote The operation/request is not acceptable by the server for some reason
     * @param message The message to return along the response(required)
     * */
    public ResponseEntity<Map<String, Object>> ResponseError406(String message){
        Map<String, Object> res= bodyGen(message, 406);
        return ResponseEntity.status(406).body(res);
    }

    /**
     * @apiNote User is not authenticated to access the required resource
     * @param message The message to return along the response(required)
     * */
    public ResponseEntity<Map<String, Object>> ResponseError401(String message){
        Map<String, Object> res= bodyGen(message, 401);
        return ResponseEntity.status(401).body(res);
    }

    /**
     * @apiNote The request went successful and the server processed it right.
     * @param message The message to return along the response(required)
     * @param data The object to send along the response
     * */
    public ResponseEntity<Map<String, Object>> ResponseOk200(String message, Object data){
        Map<String, Object> res = bodyGen(message, 200);
        if (data != null) res.put("data", data);
        return ResponseEntity.status(200).body(res);
    }

    /**
     * @apiNote If the response you require is not among the defined, this function will enable set up your custom response.
     * @param data The object to be parsed in the response(Nullable)
     * @param message The response message to return(required)
     * @param status The HTTPStatus code, must be an integer
     * */
    public ResponseEntity<Map<String, Object>> customResponse(Integer status, String message, Object data){
        Map<String, Object> res = bodyGen(message, status);
        if (data != null) res.put("data", data);
        return ResponseEntity.status(status).body(res);
    }
}
