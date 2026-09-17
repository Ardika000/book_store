package com.dproject.daniel_book_store.repository.extended.Impl;

import com.dproject.daniel_book_store.dto.projection.BookProjection;
import com.dproject.daniel_book_store.model.Book;
import com.dproject.daniel_book_store.repository.extended.BookRepositoryCustom;
import com.dproject.daniel_book_store.service.helper.QueryKey;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class BookRepositoryCustomImpl implements BookRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<BookProjection> findAllBookWithCategory() {
        String sql = "SELECT b.id AS bookId, b.bookName AS bookName, b.author AS author, "+
                "b.price AS price, c.id AS categoryId, c.name AS categoryName FROM Book b LEFT JOIN b.category c";

        List<Tuple> tuples = entityManager.createQuery(sql, Tuple.class).getResultList();

        return tuples.stream().map(tuple ->
            BookProjection.builder()
                    .bookId(tuple.get("bookId", String.class))
                    .bookName(tuple.get("bookName", String.class))
                    .author(tuple.get("author", String.class))
                    .price(tuple.get("price", BigDecimal.class))
                    .categoryId(tuple.get("categoryId", String.class))
                    .categoryName(tuple.get("categoryName", String.class))
                    .build()
        ).toList();
    }

    @Override
    public List<BookProjection> findBookWithCategoryByAuthor(String author) {
        String sql = "SELECT b.id AS bookId, b.bookName AS bookName, b.author AS author, " +
                "b.price AS price, c.id AS categoryId, c.name AS categoryName FROM Book b LEFT JOIN b.category c " +
                "WHERE b.author = :author";
        List<Tuple> tuples = entityManager.createQuery(sql, Tuple.class).setParameter("author",author).getResultList();

        return tuples.stream().map(tuple ->
                BookProjection.builder()
                        .bookId(tuple.get("bookId", String.class))
                        .bookName(tuple.get("bookName", String.class))
                        .author(tuple.get("author", String.class))
                        .price(tuple.get("price", BigDecimal.class))
                        .categoryId(tuple.get("categoryId", String.class))
                        .categoryName(tuple.get("categoryName", String.class))
                        .build()
        ).toList();
    }

    @Override
    public Page<BookProjection> findBookDynamic(QueryKey key, String value, LocalDate minDate, LocalDate maxDate, Pageable pageable) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        CriteriaQuery<BookProjection> query = builder.createQuery(BookProjection.class);
        Root<Book> bookProjectionRoot = query.from(Book.class);

        List<Predicate> predicates = new ArrayList<>();

        var joinedBookCategory = bookProjectionRoot.join("category", JoinType.LEFT);
        if(Objects.nonNull(key)){
            switch (key){
                case CATEGORYID -> predicates.add(builder.equal(joinedBookCategory.get("categoryId"), value));
                case AUTHOR_NAME -> predicates.add(builder.equal(bookProjectionRoot.get("author"), value));
                case BOOK_NAME -> predicates.add(builder.equal(bookProjectionRoot.get("bookName"), value));
//                default -> query.where();
            }
        }

        // rentang tanggal
        if(Objects.nonNull(minDate)){
            LocalDateTime startDate = minDate.atStartOfDay();
            predicates.add(builder.greaterThanOrEqualTo(bookProjectionRoot.get("create_date"), startDate));
        }

        if(Objects.nonNull(maxDate)){
            LocalDateTime startDate = maxDate.atTime(LocalTime.MAX);
            predicates.add(builder.lessThanOrEqualTo(bookProjectionRoot.get("create_date"), startDate));
        }

        if(!predicates.isEmpty())
            query.where(predicates.toArray(new Predicate[0]));

        query.multiselect(
                bookProjectionRoot.get("id"),
                bookProjectionRoot.get("bookName"),
                bookProjectionRoot.get("author"),
                bookProjectionRoot.get("price"),
                joinedBookCategory.get("id"),
                joinedBookCategory.get("name")
        );

        TypedQuery<BookProjection> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());

        List<BookProjection> resultList = typedQuery.getResultList();

        long totalRows = getTotalCount(builder, predicates);

        return new PageImpl<>(resultList, pageable, totalRows);
    }

    private long getTotalCount(CriteriaBuilder builder, List<Predicate> predicates){
        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        Root<Book> countRoot = countQuery.from(Book.class);

        if(!predicates.isEmpty())
            countQuery.where(predicates.toArray(new Predicate[0]));

        countQuery.select(builder.count(countRoot));
        return entityManager.createQuery(countQuery).getSingleResult();
    }
}
