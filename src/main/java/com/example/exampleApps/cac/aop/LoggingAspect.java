package com.example.exampleApps.cac.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect // tag is added over the aspect class, rendered by Spring boot app on boot up.
public class LoggingAspect {    // can be of any name.

    // Below @Before tag tells spring boot that it should work before the execution of method in "Pointcut". Pointcut is an expression which tells where an ADVICE should be applied.
    @Before("execution(public String com.example.exampleApps.cac.Controller.getPaymentById())")
    public void beforeMethod() {    // can be of any name
        System.out.println("inside beforeMethod Aspect");
    }


/*
Types of pointcut:
1. "execution": matches a particular method in a particular class. Access-modifier is optional to mention, and works on all 4 access-modifiers.
    Return type with classpath of method with parameter type is mandatory.
    Use of wildcards in pointcut:
      a. "*" : matches any single element(retType, packageWithClassName, methodName, arguments). Example:
        A. "*" at return type to match any return type;
        B. * com.class.path.Class.*(String) :method in Class class having 1 param of type String returning any ret type;
        C. String com.class.path.Class.methodName(*) : match given method of given return type having single param of any type.

      b. ".." : matches 0 or more element. Example:
        A. retType com.class.path.Class.methodName(..) : matches methods in Class class with given return type having any count(>=0) of arguments of any type.
        B. retType com.class..methodName()  : matches method "methodName" with no param and given return type in package "com.class" and its subpackage(s)
        C. String com.class..*()   : matches any method with return type "String" having no param in package "com.class" and its subpackage(s)

2. "within" : matches all the methods within mentioned package or class. We can do it with executions also. But this should be preferred in such cases.
    Wildcards: Example:
        A. @Before("within(com.class.path..*)")    : matches all the methods withing package "com.class.path" and its subpackages.
        B. @Before("within(com.class.path.Class)")  : matches all the methods withing class "com.class.path.Class".

3. "@within" works like the "within", but it only works with Annotations, considers all the methods of that class having this annotation, not packages or anything.
    @Before("@within(com.path.to.AnnotationOverClass)")   // All the methods of any class that have this @AnnotationOverClass. e.g.: @Before("@within(org.springframework.stereotype.Service)")

4. Similarly, for matching any method annotated with given @AnnotationOverMethod. eg: @Before("@annotation(com.path.to.AnnotationOverMethod)")

5. to match nay method with particular argument or param [ @Before("args(String, int)") ] OR [ @Before("args(com.path.to.userDefined.ClassType, ..., int,String,boolean,...)")]

6. To match any method with particular params and that param class is annotated with particular annotation. @Before("@args(org.springframework.stereotype.Service)")

7. To match any method on a particular instance of a class/interface, ie, whenever any instance of this class/interface is used to call the method into it, interceptor will get called. @Before("target(com.path.to.Class)")

8. Combining 2 pointcut expressions using boolean AND (&&) and boolean OR (||)
@Before("execution(. . . . . .)" + "&& @within(. . . . . .)")

 */

    // NAMED POINTCUT
    @Pointcut("execution(public String com.example.exampleApps.cac.Controller.*())")
    public void customPointcutName() {
        // always stays empty.
    }

    @Before("customPointcutName()")
    public void anotherBeforeMethod() {
        System.out.println("inside beforeMethod - 2 aspect");
    }


    // ADVICE - @Before(calls method by itself internally after its execution), @After(calls method itself internally before its execution), @Around(we need to call method).
    @Before("execution(* com.example.exampleApps.cac.Controller.*())")
    public void beforeMethod3(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("inside before beforeMethod3 Aspect");
        joinPoint.proceed();    // invoke intercepted method
        System.out.println("inside after beforeMethod3 Aspect");
    }
}

/**
 * If there are multiple pointcut expression, we may think that on each method invocation there will be expression match task triggered, there might be performance issue. NOO..
 * When app bootRun process starts:
 *  1. look for @Aspect annotated classes.
 *  2. parse pointcut expression ()done by PointcutParser.java class), and store it in an efficient Data structure or cache.
 *  3. look for @Component, @service, @Controller, etc., annotated class.
 *  4. For each class, it checks if its eligible for interception based on pointcut expression. (done by AbstractAutoProxyCreator.java class)
 *  5. If yes, it created proxy (proxy child class with overridden intercepted method), using CGLIB, or Dynamic Proxy. This proxy class has code which execute advice before the method, then method execution happens and after that advice if any.
 *
 *
 *  JDK Dynamic proxy is used when the (method of the) class is implementing an interface. This creates a "new" class implementing same interface but with new method body including advised tasks.
 *  CGLIB is used on those class, where it is not implementing any interface, and thus it creates a "sub" class of the same class, but with overridden methods including advised tasks in the method where required.
 */