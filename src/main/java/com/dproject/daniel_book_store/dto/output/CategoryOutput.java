package com.dproject.daniel_book_store.dto.output;

import lombok.Data;
import org.springframework.context.annotation.Configuration;

@Data
public class CategoryOutput {
    private String id;
    private String name;
    private String description;
}
