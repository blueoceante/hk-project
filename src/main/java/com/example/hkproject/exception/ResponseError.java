package com.example.hkproject.exception;

import lombok.Data;
import org.springframework.http.*;

public class ResponseError {

    // 静态方法，返回一个自定义的错误响应结构
    public static ResponseEntity<Object> error(String error, HttpStatusCode statusCode) {
        CustomErrorResponse errorResponse = new CustomErrorResponse(
                error
        );
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(errorResponse, headers, statusCode);
    }

    @Data
    private static class CustomErrorResponse {
        private String error;

        public CustomErrorResponse(String error) {
            this.error = error;
        }
    }
}
