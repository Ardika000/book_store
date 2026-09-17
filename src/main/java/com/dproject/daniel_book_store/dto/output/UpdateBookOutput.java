package com.dproject.daniel_book_store.dto.output;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBookOutput {
    private BookCategoryOutput oldData;
    private BookCategoryOutput newData;
}
