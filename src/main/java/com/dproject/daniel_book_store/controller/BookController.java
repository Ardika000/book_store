package com.dproject.daniel_book_store.controller;

import com.dproject.daniel_book_store.dto.projection.BookProjection;
import com.dproject.daniel_book_store.helper.UpdateBookHelper;
import com.dproject.daniel_book_store.helper.core.OutputSchema;
import com.dproject.daniel_book_store.model.Book;
import com.dproject.daniel_book_store.dto.request.BookRequest;
import com.dproject.daniel_book_store.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("daniel-book-store")
public class BookController {

    private final BookService bookService;

    @GetMapping("/book")
    public ResponseEntity<OutputSchema<Page<BookProjection>>> getAllBookData(
            @RequestParam(required = false, defaultValue = "") LocalDate minDate,
            @RequestParam(required = false, defaultValue = "") LocalDate maxDate,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "3") int size,
            @RequestParam(required = false, defaultValue = "bookName") String sortBy){
        return ResponseEntity.ok(OutputSchema.success(bookService.getAllDataBook(minDate, maxDate, page, size, sortBy), "Success get all book data"));
    }

    @PostMapping("/get")
    public ResponseEntity<OutputSchema<List<Book>>> getByRequirement(@RequestParam String book_name, @RequestParam String author){
        return ResponseEntity.ok(OutputSchema.success(bookService.getByNameAndAuthor(book_name, author), "Success get book data by book name & author"));
    }

//    @PostMapping("/getBook")
//    public ResponseEntity<List<Book>> getByBook(@RequestParam String book_name){
//        return bookService.getByBook(book_name);
//    }
//
//    @PostMapping("/getAuthor")
//    public ResponseEntity<List<Book>> getByAuthor(@RequestParam String author){
//        return bookService.getByAuthor(author);
//    }

    @GetMapping("/getBook/{book_name}")
    public ResponseEntity<OutputSchema<List<BookProjection>>> getByBook(@PathVariable String book_name){
        return ResponseEntity.ok(OutputSchema.success(bookService.getByBook(book_name),"Success get book by name"));
    }

    @GetMapping("/getAuthor/{author}")
    public ResponseEntity<OutputSchema<List<Book>>> getByAuthor(@PathVariable String author){
        return ResponseEntity.ok(OutputSchema.success(bookService.getByAuthor(author), "Success get book by author"));
    }

    @GetMapping("/getId/{id}")
    public ResponseEntity<OutputSchema<Book>> getBookById(@PathVariable String id){
        return ResponseEntity.ok(OutputSchema.success(bookService.getBookById(id), "Successfully retrieved book"));
    }

//    @PostMapping("/insert-book")
//    public ResponseEntity<List<Book>> insertData(@RequestBody String book_name, @RequestBody String author, @RequestBody BigDecimal price){
//        return bookService.addBook(book_name, author, price);
//    }

    @PostMapping("/insert-book")
    public ResponseEntity<OutputSchema<List<Book>>> insertData(@RequestBody @Valid BookRequest bookReq){
        return ResponseEntity.ok(OutputSchema.success(bookService.addBook(bookReq), "Successfully insert data"));
    }

    @PostMapping("/update-book/{id}")
    public ResponseEntity<OutputSchema<UpdateBookHelper>> updateData(@PathVariable String id , @RequestBody @Valid BookRequest bookRequest){
        return ResponseEntity.ok(OutputSchema.success(bookService.editBook(id, bookRequest), "Successfully update data"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<OutputSchema<Book>> deleteData(@PathVariable String id){
        return ResponseEntity.ok(OutputSchema.success(bookService.deleteBook(id),"Successfully delete data"));
    }
}
