package com.example.exampleApps.freeCodeCamp.user;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

import java.util.List;

public interface UserHttpClient {

    @GetExchange("/users")  // Can also have HttpExchange, but here we have used more specific.
    List<User> findAll();

    @GetExchange("users/{id}")
    User findById(@PathVariable Integer id);
}
