package com.dproject.daniel_book_store.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BookRequest {
    private String bookName;
    private String author;
    private BigDecimal price;

}
