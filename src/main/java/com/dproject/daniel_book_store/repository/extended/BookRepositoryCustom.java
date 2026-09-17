package com.dproject.daniel_book_store.repository.extended;


import com.dproject.daniel_book_store.dto.projection.BookProjection;
import com.dproject.daniel_book_store.service.helper.QueryKey;

import java.util.List;

public interface BookRepositoryCustom {
    List<BookProjection> findAllBookWithCategory();
    List<BookProjection> findBookWithCategoryByAuthor(String author);
    List<BookProjection> findBookDynamic(QueryKey key, String value);
}
