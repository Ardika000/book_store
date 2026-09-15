package com.dproject.daniel_book_store.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
@ToString
public class RequestLoggerConfiguration {
    private boolean includeClientInfo = false;
    private boolean includeHeaders = true;
    private boolean includeQueryString = true;
    private boolean includePayload = true;
    private Integer maxPayloadLength = 10_000;
}
