package com.dproject.daniel_book_store;

import com.dproject.daniel_book_store.model.Book;
import com.dproject.daniel_book_store.repository.BookRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Configuration
public class SeedingDatabase {
    @Bean
    CommandLineRunner initDatabase(BookRepository bookRepository){
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
        };
    }
}
