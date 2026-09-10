package com.dproject.daniel_book_store.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "Activity_Log")
public class ActivityLog {
    @Id
    @Column(length = 32)
    private String id;

    @PrePersist
    public void generateId(){
        if(this.id == null)
            this.id = UUID.randomUUID().toString().replace("-", "");
    }

    private String activity;

    @Column(length = 50)
    private String bookNameOld;

    @Column(length = 50)
    private String bookNameNew;

    @Column(length = 25)
    private String authorOld;

    @Column(length = 25)
    private String authorNew;

    private BigDecimal priceOld;
    private BigDecimal priceNew;

    private LocalDateTime createDate;

}
