package com.example.txlearn.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 处理所有不可知的异常
     */
    @ExceptionHandler(Throwable.class)
    public ResponseEntity handleException(Throwable e){
        // 打印堆栈信息
        return buildErrorResponseEntity(e.getMessage());
    }

    private static ResponseEntity  buildErrorResponseEntity(String msg){
        Map<String,String> map  = new HashMap<>();
        map.put("success","0");
        map.put("msg",msg);
        final ResponseEntity<Map<String, String>> responseEntity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(map);
        return responseEntity;
    }

}
