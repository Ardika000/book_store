package com.dproject.daniel_book_store;

import com.dproject.daniel_book_store.model.Book;
import com.dproject.daniel_book_store.model.Category;
import com.dproject.daniel_book_store.repository.BookRepository;
import com.dproject.daniel_book_store.repository.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Configuration
public class SeedingDatabase {
    @Bean
    CommandLineRunner initDatabase(BookRepository bookRepository, CategoryRepository categoryRepository){
        return args -> {
          if(bookRepository.count() == 0){
              Book book1 = new Book();
              book1.setBookName("The Lord Of The Rings");
              book1.setAuthor("J. R. R. Tolkien");
              book1.setPrice(BigDecimal.valueOf(927000));
              book1.setCreate_date(LocalDateTime.now());
              book1.setUpdate_date(LocalDateTime.now());

              bookRepository.saveAll(List.of(book1));
              System.out.println("please masuk datanya");
          }

          if(categoryRepository.count() == 0){
              Category category = new Category();
              category.setName("Default");
              category.setDescription("This is default category for existing books");

              categoryRepository.save(category);
              Category defaultCategory = categoryRepository.findByName("Default");

              List<Book> existingBooks = bookRepository.findAll();

              for (Book book : existingBooks){
                  if(book.getCategory() == null){
                      book.setCategory(defaultCategory);
                  }
              }

              bookRepository.saveAll(existingBooks);
          }


        };
    }
}
