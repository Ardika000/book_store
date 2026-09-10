package com.dproject.daniel_book_store.helper.core;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OutputSchema<T> {
    private String statusCode;
    private String message;
    private T data;
    private ErrorSchema error;
    private LocalDateTime timestamp;

    public static <T> OutputSchema<T> success(T data, String message){
        return OutputSchema.<T>builder()
                .statusCode("200")
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();

    }

    public static <T> OutputSchema<T> failure(String errorCode, String message) {
        return OutputSchema.<T>builder()
                .statusCode("ERROR")
                .message("Request failed")
                .error(ErrorSchema.builder()
                        .errorCode(errorCode)
                        .message(message)
                        .timestamp(LocalDateTime.now())
                        .build())
                .timestamp(LocalDateTime.now())
                .build();
    }

}
