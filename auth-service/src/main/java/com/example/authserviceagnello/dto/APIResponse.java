package com.example.authserviceagnello.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class APIResponse<T> {
    private APIResponseStatus status;
    private String message;
    private HttpStatus httpStatus;
    private LocalDateTime timestamp;
    private T data;

    public static <T> APIResponse<T> success(T data, String message) {
        return APIResponse.<T>builder()
                .status(APIResponseStatus.SUCCESS)
                .message(message)
                .httpStatus(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .data(data)
                .build();
    }

    public static <T> APIResponse<T> error(String message, HttpStatus httpStatus) {
        return APIResponse.<T>builder()
                .status(APIResponseStatus.ERROR)
                .message(message)
                .httpStatus(httpStatus)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> APIResponse<T> warning(String message, T data) {
        return APIResponse.<T>builder()
                .status(APIResponseStatus.WARNING)
                .message(message)
                .httpStatus(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .data(data)
                .build();
    }

    public static <T> APIResponse<T> info(String message, T data) {
        return APIResponse.<T>builder()
                .status(APIResponseStatus.INFO)
                .message(message)
                .httpStatus(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .data(data)
                .build();
    }
} 