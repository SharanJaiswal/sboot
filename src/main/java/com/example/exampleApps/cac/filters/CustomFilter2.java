package com.example.exampleApps.cac.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class CustomFilter2 extends OncePerRequestFilter implements Ordered {

    @Autowired private ApplicationContext applicationContext;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("Inside custom filter 2.");
        filterChain.doFilter(request, response);
        System.out.println("Exiting filter 2");
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

    @Override
    public int getOrder() {     //not very good approach if this filter is being used by others via libs.
        if(applicationContext.containsBean("customFilter2")) {
            return -1;
        }
        return LOWEST_PRECEDENCE;
    }
}
