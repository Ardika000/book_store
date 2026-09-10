package com.dproject.daniel_book_store.helper;

import com.dproject.daniel_book_store.model.Book;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBookHelper {
    private Book oldBook;
    private Book newBook;
}
