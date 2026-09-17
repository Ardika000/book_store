package com.dproject.daniel_book_store.dto.output;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookCategoryOutput {
    private String bookId;
    private String bookName;
    private String author;
    private BigDecimal price;
    private String categoryId;
    private String categoryName;

}
