package com.dproject.daniel_book_store.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
@Slf4j
public class RequestPerformenceFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        StopWatch sw = new StopWatch();
        sw.start();

        String uri = request.getRequestURI();
        String method = request.getMethod();

        try {
            String traceID = request.getHeader("X-Trace-Id");
            if(traceID == null || traceID.isEmpty()){
                traceID = UUID.randomUUID().toString().replace("-", "");
            }
            MDC.put("traceId", traceID);

            filterChain.doFilter(request, response);
        }finally {
            sw.stop();
            long excutionTime = sw.getTotalTimeMillis();

            response.setHeader("X-response-time: ", excutionTime + "ms");

            int status = response.getStatus();
            if(status >= 500){
                log.error("HTTP: {} {} | Status: {} | Execution Time: {} ms", method, uri, status, excutionTime);
            } else if (status >= 400) {
                log.warn("HTTP: {} {} | Status: {} | Execution Time: {} ms", method, uri, status, excutionTime);
            }else {
                log.info("HTTP: {} {} | Status: {} | Execution Time: {} ms", method, uri, status, excutionTime);
            }
        }
        MDC.clear();
    }
}
