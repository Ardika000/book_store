package com.dproject.daniel_book_store.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BookRequest {
    @NotBlank(message = "Book name must be filled")
    @Size(max = 50, message = "Length of book name maximal 50 character")
    private String bookName;

    @NotBlank(message = "Author must be filled")
    @Size(max = 25, message = "Length of author maximal 25 character")
    private String author;

    @NotNull(message = "Price can't be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal price;

}
