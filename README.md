!!!! READ THIS AS TEXT FILE. DO NOT USE MARKDOWN RENDERER !!!!

Benefit of Spring Framework over Servlet:
1. Removal of web.xml: (A) single file over the time it becomes too big and hence difficult to manage and comprehend by humans. (B) Spring introduces annotations based configurations.
2. IoC - Inversion of Control: It is a flexible way to manage object dependencies and its lifecycle through dependency injection. Strictly, Dependency injection is an implementation of IoC.
3. Unit testing is much harder in servlets, mocking is not easy because of tight coupling of dependency classes whose objects are managed by servlets. Spring dependency injection removes this tight coupling.
4. Difficult to manage REST APIs because handling different HTTP methods, req params, path mapping make code a little difficult to understand. Spring MVC provides an organised approach to handle the request and it's easy to build RESTful APIs.
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
BeanLifecycle::: AppStart -> start IoC container -> resolve dependency injected through constructor, adn then Construct Beans and run constructor -> Inject dependency into constructed beans(using reflection - field and setter injection) -> @PostConstruct -> Use Bean -> @PreDestroy -> Bean Destroyed.

Bean Initialization: Eager - Lazy

BEAN SCOPES: Singleton, Prototype, Request, Session, Application
.Singleton: Default, Only 1 instance eagerly instantiated per IoC|appOf1IoC, @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON) aka @Scope("singleton")-value of enum.
.Prototype: Lazy initialized everytime, when it is "actually" needed, @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE) aka @Scope("prototype")-value of enum.
.Request: Lazy, new object is created ONCE per request for each HTTP request for all classes which serves this request which are marked as "request" scope, but not all classes which serves this request; 
For eagerly initialized dependent objects, depending on "request" scoped class objects, application startup will fail because no object will be created during dependency injection of classes scoped "request". Add @Scope(value="request", proxyMode=ScopedProxyMode.TARGET_CLASS) over "request" scope classes, if their objects are dependency of eagerly initialized singleton scope classes. This creates a dummy obj and inject it unless any HTTP request hits.
.Session: Lazy, HTTP session can generally handle multiple HTTP requests. Each session has some default time which we can override it using "servlet.session.expiry" property. 
HttpServletRequest - HttpSession:  request.getSession().invalidate();   // to end current http session.
.Application: Eager, like singleton, but if we want to share an object in multiple IoC running application. Not much used.


@Profile is technically intended for environment separation rather than application specific bean creation. Environments like dev,qa,prod, etc. where certain things varies based on envs like DB credentials, conn-req-session timeouts, URL, ports, throttle values, retry values, etc.
We put different profiles properties in properties file. eg. application-{profile_x}.property=value. We can put default values in application.properties(default) file. {dev|aq|prod} values in application-{dev|aq|prod}.properties file. If we have different files, then property name can be allowed same in all files, if needed.
To pick specific prop file, mention a property along with default properties if any, in default file -> spring.profile.active=dev|aq|prod   -> This will first see in specified profile, and if absent in that file, then in default file. But this is hard-coded.
We know, when we bootup our app in intellij, it runs "mvn spring-boot:run" which picks application.properties file by default. But if set the env variable such that when app bootUps, it picks prod profile.
To do that, we provide env var "-Dspring-boot.run.profiles=prod". Or when we run the app, we can use command "mvn spring-boot:run -Dspring-boot.run.profiles=prod". Specifying env var for profile, will override the "spring.profiles.active" property, even if it is mentioned inside the application.properties file.
See pom.xml also where <profile> annotation is present.
We use @Profile("dev|aq|prod") over class to tell spring to only handle the class and create its bean when active profile value matches with this annotation value. Otherwise, skip it.
Also, we can provide multiple active profiles, separated by commas. Although both are considered as active profiles, but in precedence order, last will be given more preference for first looking for property. eg, spring.profiles.active=prod,qa

AOP(Aspect Oriented Programming): Helps to intercept the method invocation. To perform some task before and after the method. This way, focus is on business logic by handling boilerplate and repetitive code like logging, transaction mgmt, Security, etc.
Aspect is a module to handle these repetitive or boilerplate code. Helps in achieving reusability, maintainability of code.


Critical section: code segment where shared resources are being accessed and modified. Solution is achieved using transaction based on ACID principles. Atomicity: if any operation fails, entire txn rollbacks. Consistency: Before and after the trxn, the resource should be consistent even after failure. Isolation: individual txns are isolated in multi txn space. Durability: once txn is complete, data persist even in system crash.