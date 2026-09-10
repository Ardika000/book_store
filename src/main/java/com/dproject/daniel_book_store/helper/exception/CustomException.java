package com.dproject.daniel_book_store.helper.exception;

import com.dproject.daniel_book_store.helper.core.ErrorEnum;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException{
    private final ErrorEnum errorEnum;

    public CustomException(ErrorEnum errorEnum){
        super(errorEnum.getMessageEnglish());
        this.errorEnum = errorEnum;
    }

    public CustomException(ErrorEnum errorEnum, String customMessage){
        super(customMessage);
        this.errorEnum = errorEnum;
    }

}
