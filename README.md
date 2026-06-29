!!!! READ THIS AS TEXT FILE. DO NOT USE MARKDOWN RENDERER !!!!

Servelet =/= Spring Framework =/= SpringMVC =/= SpringBoot

"SERVLET" is a java class which handles client request, processes it, and return the response. "SERVLET CONTAINERS" are the one which manages servlets. Servlets are at the core of spring and springboot.
SEE src/main/resources/ServletExample1.txt for more details.

"SPRING FRAMEWORK" is a framework which provides a comprehensive programming and configuration model for modern Java-based enterprise applications. It is built on top of servlets and provides a lot of features like dependency injection, aspect-oriented programming, transaction management, etc.
SEE src/main/resources/SpringFramework.txt for more details.

"SPRING BOOT" is a framework which provides a lot of features like auto-configuration, embedded servlet container, etc. It is built on top of spring framework and provides a lot of features like dependency injection, aspect-oriented programming, transaction management, etc.
SEE src/main/resources/SpringBoot.txt for more details.

================================================================================================================================================

JAR vs WAR: JAR is a Java Archive file which contains Java classes and other resources like images, properties files, etc. It is used to package a standaloneJava applications. WAR is a Web Archive file which contains Java classes, JSP files, HTML files, images, properties files, etc. It is used to package web applications.

================================================================================================================================================

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

================================================================================================================================================
package com.example.exampleApps.SpringbootApplication
================================================================================================================================================

SEE src/main/resources/BeanLifecycle.txt for more details.

================================================================================================================================================
package com.example.exampleApps.cac.Controller.PaymentController
================================================================================================================================================

@Profile is technically intended for environment separation rather than application specific bean creation. Environments like dev,qa,prod, etc. where certain things varies based on envs like DB credentials, conn-req-session timeouts, URL, ports, throttle values, retry values, etc.
We put different profiles properties in properties file. eg. application-{profile_x}.property=value. We can put default values in application.properties(default) file. {dev|qa|prod} values in application-{dev|aq|prod}.properties file. If we have different files, then property name can be allowed same in all files, if needed.
To pick specific prop file, mention a property along with default properties if any, in default file -> spring.profile.active=dev|aq|prod -> This will first see in specified profile, and if absent in that file, then in default file. But this is hard-coded.
We know, when we bootup our app in intellij, it runs "mvn spring-boot:run" which picks application.properties file by default. But if set the env variable such that when app bootUps, it picks prod profile.
To do that, we provide env var "-Dspring-boot.run.profiles=prod". Or when we run the app, we can use command "mvn spring-boot:run -Dspring-boot.run.profiles=prod". Specifying env var for profile, will override the "spring.profiles.active" property, even if it is mentioned inside the application.properties file.
See pom.xml also where <profile> annotation is present.
We use @Profile("dev|aq|prod") over class to tell spring to only handle the class and create its bean when active profile value matches with this annotation value. Otherwise, skip it.
Also, we can provide multiple active profiles, separated by commas. Although both are considered as active profiles, but in precedence order, last will be given more preference for first looking for property. eg, spring.profiles.active=prod,qa

AOP(Aspect Oriented Programming): Helps to intercept the method invocation. To perform some task before and after the method. This way, focus is on business logic by handling boilerplate and repetitive code like logging, transaction mgmt, Security, etc.
Aspect is a module to handle these repetitive or boilerplate code. Helps in achieving reusability, maintainability of code.

Interceptors: To intercept HTTP req/resp and add logic specific to particular servlet, placed/Invoked somewhere between dispatcher servlet and actual controller invocation, or before invocation of any downstream method/constructor/ElementType.{...} . We can have many of it and thus ordering b/n them can also be defined.
To invoke interceptor before controller, make custom interceptor class and register each individual interceptor in configuration file. In DispatcherServlet.class, in doDispatch() method, we see sequence of applyPreHandle() call -> handle() where controller gets invoked -> applyPostHandle() -> afterCompletion() in both exception or no exception case.
To invoke it before any downstream method, make custom annotation -> using AOP intercept method having this custom annotation with @Around advise.
Interceptors are very specific to the Servlets.

Filter: To intercept HTTP req/resp and add logic agnostic of underlying servlet. There can be many filters and ordering b/n them. filters are between Servlet container and servlets. They are generally defined for all the servlets (Dispatcher servlet is one ege of it in case of springboot).
It is WebServerContainer concept that can be run on any application that runs in a web container such as TOMCAT, JBOSS, ETC. There can be multiple filter chain also. Each can track individually the req as well as response, whose orders are same as of interceptors.
Open ApplicationFilterChain.java class provided by tomcat out of the box. Place debug at filters array, and we'll see our filters also in the chain. Last one being Tomcat Web Socket filter, which upgrades traditional HTTP connection by full duplex real-time connection b/n cli-ser. Hence we need to make sure all HTTP filters are placed before it.
If custom filters don't have user assigned order, then @Order(LOWEST_PRECEDENCE) is automatically is annotated over that filter class.
If we place web-socket conn before other http filters like authorization and authentication, then filters ordered below them will simply not execute, hence might be breaking imp part of app.

Critical section: code segment where shared resources are being accessed and modified. Solution is achieved using transaction based on ACID principles. Atomicity: if any operation fails, entire txn rollbacks. Consistency: Before and after the trxn, the resource should be consistent even after failure. Isolation: individual txns are isolated in multi txn space. Durability: once txn is complete, data persist even in system crash.

https://youtu.be/_Tby7sRA4hs?si=sw9zdr3f38WSn6nJ
