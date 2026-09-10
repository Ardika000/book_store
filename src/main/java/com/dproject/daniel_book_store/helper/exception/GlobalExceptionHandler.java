package com.dproject.daniel_book_store.helper.exception;

import com.dproject.daniel_book_store.helper.core.ErrorEnum;
import com.dproject.daniel_book_store.helper.core.OutputSchema;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@AllArgsConstructor
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<OutputSchema<Object>> handleCustomException(CustomException ex){
        System.out.println("Global Ecception Handler bekerja !!!");
        ErrorEnum err = ex.getErrorEnum();
        OutputSchema<Object> response = OutputSchema.failure(err.getErrorCode(), ex.getMessage());
        return new ResponseEntity<>(response, err.getHttpStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<OutputSchema<Object>> handleGeneralException(Exception ex){
        OutputSchema<Object> response = OutputSchema.failure(
                ErrorEnum.INQUIRY_FAILED.getErrorCode(), ex.getMessage()
        );

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
