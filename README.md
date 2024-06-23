!!!! READ THIS AS TEXT FILE. DO NOT USE MARKDOWN RENDERER !!!!

Benefit of Spring Framework over Servlet:
1. Removal of web.xml: (A) single file over the time it becomes too big and hence difficult to manage and comprehend by humans. (B) Spring introduces annotations based configurations.
2. IoC - Inversion of Control: It is a flexible way to manage object dependencies and its lifecycle through dependency injection. Strictly, Dependency injection is an implementation of IoC.
3. Unit testing is much harder in servlets, mocking is not easy because of tight coupling of dependency classes whose objects are managed by servlets. Spring dependency injection removes this tight coupling.
4. Difficult to manage REST APIs because handling different HTTP methods, req params, path mapping make code a little difficult to understand. Spring MVC provides an organised approach to handle the request and its easy to build RESTful APIs.
5. Easy integration with other technologies like hibernate, adding security, etc.
6. Allows developers to choose different combination of technologies and framework of choice. Eg., Junit|Mockito for unit testing; Hibernate|JDBC|JPA; Asynchronous programming; caching; messaging; security, etc.


PHASES OF MAVEN LIFECYCLE: First 7 are phases and also all are maven commands. Run any from 1 to 7, and it will run all the phases before it sequentially till that phase.
1. validate: project structure
2. compile: Source code
3. test: Unit test of codes
4. package: Compiled code into packages of JAR|WAR etc.
5. verify: Package integrity
6. install: moved packed package to "~/.m2/"
7. deploy: move package to remote repository
8. clean: deleted target folder and its contents
9. site: 

We can add user defined task inside any phase. This can be achieved using pom.xml inside <build> ... </build> tag. Maven has already added all first 7 phases automatically, but we can override them.

Spring Beans: Java objects managed by Spring container (IoC container, contains and manages all beans which get created in them using @Component|@Bean). Created using @ComponentScan and classes with @Configuration during app bootUp.
Time of object initialization: Eagerness(when booting up application, with Scope as singleton, default); Lazy(when needed, with scope Prototype, classes with @Lazy annotation initialized weather it's with @Component or w/o or with any bean initialization annotation).
BeanLifecycle::: AppStart -> start IoC container -> Construct Beans -> Inject dependency into constructed beans -> @PostConstruct -> Use Bean -> @PreDestroy -> Bean Destroyed.
