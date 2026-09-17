package com.dproject.daniel_book_store.repository;

import com.dproject.daniel_book_store.dto.projection.BookProjection;
import com.dproject.daniel_book_store.model.Book;
import com.dproject.daniel_book_store.repository.extended.BookRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, String>, BookRepositoryCustom {
    List<Book> findByBookNameAndAuthor(String book_name, String author);
    List<BookProjection> findByBookName(String book_name);
    List<Book> findByAuthor(String author);

    @Query("SELECT b FROM Book AS b WHERE " +
            "(CAST(:minDate AS TIMESTAMP) IS NULL OR b.create_date >= :minDate) AND " + "(CAST(:maxDate AS TIMESTAMP) IS NULL OR b.create_date <= :maxDate)")
    Page<BookProjection> filterByDateRange(@Param("minDate")LocalDateTime minDate, @Param("maxDate") LocalDateTime maxDate, Pageable pageable);
}
