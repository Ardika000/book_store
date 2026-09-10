package com.dproject.daniel_book_store.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "Book")
public class Book {
    @Id
    @Column(length = 32)
    private String id;

    @PrePersist
    public void generateId(){
        if(this.id == null)
            this.id = UUID.randomUUID().toString().replace("-", "");
    }

    @Column(length = 50)
    private String bookName;

    @Column(length = 25)
    private String author;
    private BigDecimal price;
    private LocalDateTime create_date;
    private LocalDateTime update_date;

}
