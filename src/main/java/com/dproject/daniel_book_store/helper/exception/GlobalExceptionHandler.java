package com.dproject.daniel_book_store.helper.exception;

import com.dproject.daniel_book_store.helper.core.ErrorEnum;
import com.dproject.daniel_book_store.helper.core.OutputSchema;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@AllArgsConstructor
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<OutputSchema<Object>> handleCustomException(CustomException ex){
        ErrorEnum err = ex.getErrorEnum();
        log.warn("Business exception occured: [ErrorCode: {}] - {}", err.getErrorCode(), ex.getMessage());
        OutputSchema<Object> response = OutputSchema.failure(err.getErrorCode(), ex.getMessage());
        return new ResponseEntity<>(response, err.getHttpStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<OutputSchema<Object>> handleGeneralException(Exception ex){
        log.error("Unhandled System Exception: ", ex);
        OutputSchema<Object> response = OutputSchema.failure(
                ErrorEnum.INQUIRY_FAILED.getErrorCode(), ex.getMessage()
        );

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
