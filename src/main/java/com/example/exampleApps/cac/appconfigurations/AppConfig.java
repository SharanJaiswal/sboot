package com.example.exampleApps.cac.appconfigurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration  // Way to override "convention over configuration", where we provide configuration to spring boot to override specific autoconfiguration.
// Higher precedence over autoconfiguration. This contains @Component inside it.
public class AppConfig {
//    @Bean   // Here, as external configuration, we are telling Springboot to create an object of some User class, because we cannot use @Component over User class as we don't its default constructor.
//    public User createUserBean() {    // we can give here any method name. But return type decides here which class' object needs to be created.
//        return new User("defaultUserName", "defaultEmail");
//    }
//    If for certain class Bean as well as Component is present, then Bean will be given precedence as it is inside configuration class. I'm unsure about if both will get created or not. Use @Primary to give precedence maybe.

//    If more than 1 bean of same type is present, then IoC will create and manage both bean, give them different names. We can also give them name using qualifier.
//    @Bean   // It is method level annotation unlike @Component which is class level annotation.
//    public User createUserBean() {
//        return new User("specificUserName", "specificEmail");
//    }
}
