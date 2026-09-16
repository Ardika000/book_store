package com.dproject.daniel_book_store.service;

import com.dproject.daniel_book_store.dto.output.CategoryOutput;
import com.dproject.daniel_book_store.dto.request.CategoryRequest;
import com.dproject.daniel_book_store.helper.core.OutputSchema;
import com.dproject.daniel_book_store.model.Category;
import com.dproject.daniel_book_store.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public List<Category> getAllData() {
        return categoryRepository.findAll();
    }


    public List<Category> addDataCat(CategoryRequest categoryRequest) {
        Category newCat = new Category();
        newCat.setName(categoryRequest.getName());
        newCat.setDescription(categoryRequest.getDescription());
        var savedCat = categoryRepository.save(newCat);
        return categoryRepository.findAll();
    }
}
