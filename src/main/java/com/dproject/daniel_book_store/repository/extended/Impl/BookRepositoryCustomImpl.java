package com.dproject.daniel_book_store.repository.extended.Impl;

import com.dproject.daniel_book_store.dto.projection.BookProjection;
import com.dproject.daniel_book_store.model.Book;
import com.dproject.daniel_book_store.repository.extended.BookRepositoryCustom;
import com.dproject.daniel_book_store.service.helper.QueryKey;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
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
    public List<BookProjection> findBookDynamic(QueryKey key, String value) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        CriteriaQuery<BookProjection> query = builder.createQuery(BookProjection.class);
        Root<Book> bookProjectionRoot = query.from(Book.class);

        var joinedBookCategory = bookProjectionRoot.join("category", JoinType.LEFT);
        if(Objects.nonNull(key)){
            switch (key){
                case CATEGORYID -> query.where(builder.equal(joinedBookCategory.get("categoryId"), value));
                case AUTHOR_NAME -> query.where(builder.equal(bookProjectionRoot.get("author"), value));
                case BOOK_NAME -> query.where(builder.equal(bookProjectionRoot.get("bookName"), value));
                default -> query.where();
            }
        }

        query.multiselect(
                bookProjectionRoot.get("id"),
                bookProjectionRoot.get("bookName"),
                bookProjectionRoot.get("author"),
                bookProjectionRoot.get("price"),
                joinedBookCategory.get("id"),
                joinedBookCategory.get("name")
        );

        return entityManager.createQuery(query).getResultList();
    }
}
