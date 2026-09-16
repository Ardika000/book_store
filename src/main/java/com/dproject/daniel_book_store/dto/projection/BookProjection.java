package com.dproject.daniel_book_store.dto.projection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.PrivateKey;


public interface BookProjection {
    String getId();
    String getBookName();
    String getAuthor();
    BigDecimal getPrice();
}
