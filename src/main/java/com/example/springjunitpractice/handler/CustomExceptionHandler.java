package com.example.springjunitpractice.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.springjunitpractice.dto.ResponseDto;
import com.example.springjunitpractice.handler.exception.CustomApiException;

@RestControllerAdvice // RestController에서 발생한 모든 Exception을 낚아채서 처리함
public class CustomExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(getClass());

    // CustomApiException 발생 시 호출됨
    @ExceptionHandler(CustomApiException.class)
    public ResponseEntity<?> apiException(CustomApiException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(new ResponseDto<>(-1, e.getMessage(), null), HttpStatus.BAD_REQUEST);
    }
}
