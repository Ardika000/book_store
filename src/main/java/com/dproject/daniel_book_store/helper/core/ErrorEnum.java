package com.dproject.daniel_book_store.helper.core;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Getter
//@RequiredArgsConstructor
public enum ErrorEnum {
    SUCCESS_RESPONSE("SCSS-BS-00", "Success", HttpStatus.OK),
    ERROR_NOT_FOUNT("ERR-BS-NF-00", "Book Not Found", HttpStatus.NOT_FOUND),
    ERROR_INPUT("INV-BS-INP-00", "Invalid Input", HttpStatus.BAD_REQUEST),
    INQUIRY_FAILED("ERR-BS-FL-01", "Failed", HttpStatus.INTERNAL_SERVER_ERROR),
    DUPLICATE_DATA("ERR_BS_DT_02", "Data already exists in database", HttpStatus.CONFLICT);

    private final String errorCode;
    private final String messageEnglish;
    private final HttpStatus httpStatus;

    ErrorEnum(String errorCode, String messageEnglish, HttpStatus httpStatus) {
        this.errorCode = errorCode;
        this.messageEnglish = messageEnglish;
        this.httpStatus = httpStatus;
    }

}
