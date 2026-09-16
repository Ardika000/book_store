package com.dproject.daniel_book_store.repository;

import com.dproject.daniel_book_store.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {

    Category findByName(String aDefault);
}
