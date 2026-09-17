package com.dproject.daniel_book_store.dto.projection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookProjection {
    private String bookId;
    private String bookName;
    private String author;
    private BigDecimal price;
    private String categoryId;
    private String categoryName;
}
