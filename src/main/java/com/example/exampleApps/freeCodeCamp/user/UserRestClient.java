package com.example.exampleApps.freeCodeCamp.user;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component  // We want to tell Spring that put this class' instance into the application context and manage it to pull that instance whenever needed in the project during runtime.
public class UserRestClient {

    private final RestClient restClient;    // We cannot create an instance of this RestClient, as this is an interface. Hence, we cannot inject the object tof it. But, open this interface.

    public UserRestClient (RestClient.Builder builder) {
        JdkClientHttpRequestFactory jdkClientHttpRequestFactory = new JdkClientHttpRequestFactory();
        jdkClientHttpRequestFactory.setReadTimeout(5000);


        this.restClient = builder
                .baseUrl("https://jsonplaceholder.typicode.com/")
//                .requestFactory(new JdkClientHttpRequestFactory())   // There is default HTTP client under the hood but we can swap it by passing the object of instance of  the different type of client
//                .defaultHeader("User-Agent", "Spring 5 WebClient")    // Setting default header also.
//                .requestInterceptor()
                .build();
    }

    public List<User> findAll() {
        return restClient.get()
                .uri("/users")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    public User findById(Integer id) {
        return restClient.get()
                .uri("/users/{id}", id)
                .retrieve()
                .body(User.class);
    }

}
