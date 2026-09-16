package com.dproject.daniel_book_store.config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StopWatch;
import org.springframework.web.filter.AbstractRequestLoggingFilter;

import java.util.UUID;

@Configuration
@Slf4j
public class CustomRequestLoggingFilter extends AbstractRequestLoggingFilter {
    private final RequestLoggerConfiguration requestLoggerConfiguration;

    public CustomRequestLoggingFilter(
            RequestLoggerConfiguration requestLoggerConfiguration
    ){
        super();
        log.info("Request Logger {}", requestLoggerConfiguration);
        super.setIncludeClientInfo(requestLoggerConfiguration.isIncludeClientInfo());
        super.setIncludeHeaders(requestLoggerConfiguration.isIncludeHeaders());
        super.setIncludeQueryString(requestLoggerConfiguration.isIncludeQueryString());
        super.setIncludePayload(requestLoggerConfiguration.isIncludePayload());
        super.setMaxPayloadLength(requestLoggerConfiguration.getMaxPayloadLength());
        super.setAfterMessagePrefix("");
        super.setAfterMessageSuffix("");
        this.requestLoggerConfiguration = requestLoggerConfiguration;
    }

    @Override
    protected void beforeRequest(HttpServletRequest request, String message) {
        StopWatch sw = new StopWatch();
        sw.start();

        request.setAttribute("stopwatch", sw);

        log.info("== Processing {} {} ==", request.getMethod(), request.getRequestURI());
    }

    @Override
    protected void afterRequest(HttpServletRequest request, String message) {
        try {
            long executionTime = 0;
            StopWatch swGet = (StopWatch) request.getAttribute("stopwatch");
            if(swGet != null && swGet.isRunning()){
                swGet.stop();
                executionTime = swGet.getTotalTimeMillis();
            }

            log.debug(message);
            log.info("Execution Time: {} ms", executionTime);
            log.info("== Finish {} {} ==", request.getMethod(), request.getRequestURI());
        }finally {
            MDC.clear();
        }
    }
}
