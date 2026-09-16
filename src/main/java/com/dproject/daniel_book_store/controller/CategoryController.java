package com.dproject.daniel_book_store.controller;

import com.dproject.daniel_book_store.dto.output.CategoryOutput;
import com.dproject.daniel_book_store.dto.request.CategoryRequest;
import com.dproject.daniel_book_store.helper.core.OutputSchema;
import com.dproject.daniel_book_store.model.Category;
import com.dproject.daniel_book_store.repository.CategoryRepository;
import com.dproject.daniel_book_store.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.hibernate.result.Output;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("daniel-book-store")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/getAllCat")
    public ResponseEntity<OutputSchema<List<Category>>> showAllCategory(){
        return ResponseEntity.ok(OutputSchema.success(categoryService.getAllData(), "Success get all category data"));
    }

    @PostMapping("/addCat")
    public ResponseEntity<OutputSchema<List<Category>>> addCategory(@RequestBody CategoryRequest categoryRequest){
        return ResponseEntity.ok(OutputSchema.success(categoryService.addDataCat(categoryRequest), "Success add category data"));
    }
}
