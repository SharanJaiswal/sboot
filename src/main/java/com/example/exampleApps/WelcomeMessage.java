package com.example.exampleApps;

import org.springframework.stereotype.Component;

/**
 * "default package": If you don't have any package defined at the top, then it must be scr>main>java, a.k.a. default-package.
 */
// If we will be removing this @Component annotation, then wherever we are using @Autowired, there will be error, as bean of this class won't present in the Springboot Context.
@Component  // We are writing this to make this class known to Springboot framework. In general, all classes with @Component are available in big container managed by Springboot Framework, called Application context.
public class WelcomeMessage {
    public String getWelcomeMessage() {
        return "Welcome to Spring Boot Application";
    }
}
