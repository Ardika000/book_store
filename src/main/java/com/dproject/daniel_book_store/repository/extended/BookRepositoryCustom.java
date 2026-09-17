package com.dproject.daniel_book_store.repository.extended;


import com.dproject.daniel_book_store.dto.projection.BookProjection;
import com.dproject.daniel_book_store.service.helper.QueryKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface BookRepositoryCustom {
    List<BookProjection> findAllBookWithCategory();
    List<BookProjection> findBookWithCategoryByAuthor(String author);
    Page<BookProjection> findBookDynamic(
            QueryKey key, String value,
            LocalDate minDate,
            LocalDate maxDate,
            Pageable pageable
    );
}
