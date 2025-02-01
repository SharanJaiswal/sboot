package com.example.exampleApps.cac.filters;

//import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

//public class CustomFilter1 implements Filter {}   // We'll implement OncePerRequestFilter, best in cases for apps having multiple servlets

@Component
public class CustomFilter1 extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("In filter 1");
        Collections.list(request.getHeaderNames())
                        .forEach(header ->
                                System.out.println("Header:" + header + " = " + request.getHeader(header)));
        filterChain.doFilter(request, response);

        System.out.println("Exiting filter 1");
    }

    @Override
    public void destroy() { // Just before destroying the object of this class
//        super.destroy();
        // We can put our custom logic
    }

    @Override
    protected void initFilterBean() throws ServletException {   // When this bean loads for the first time, perform this logic.
//        super.initFilterBean();
        // We can put our custom logic
    }
}
