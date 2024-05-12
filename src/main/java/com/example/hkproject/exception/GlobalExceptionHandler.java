package com.example.hkproject.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        StringBuilder errors = new StringBuilder();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String errorMessage = error.getDefaultMessage();
            errors.append(errorMessage).append("; ");
        });
        log.error("Parameter error: {}", errors);
        return ResponseError.error("Parameter error: " + errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TakeOrderException.class)
    @ResponseBody
    public ResponseEntity<Object> handleTakeOrderException(TakeOrderException ex) {
        log.error("Take order error: {}", ex.getMessage());
        return ResponseError.error(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // 捕获所有的Exception
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
        log.error("Internal server error: {}", ex.getMessage());
        return ResponseError.error(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}


