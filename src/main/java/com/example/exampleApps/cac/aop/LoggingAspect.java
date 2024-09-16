package com.example.exampleApps.cac.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Component
@Aspect // tag is added over the aspect class, rendered by Spring boot app on boot up.
public class LoggingAspect {    // can be of any name.

    // Below @Before tag tells spring boot that it should work before the execution of method in "Pointcut". Pointcut is an expression which tells where an ADVICE should be applied.
    @Before("execution(public String com.example.exampleApps.cac.Controller.getPaymentById())")
    public void beforeMethod() {    // can be of any name
        System.out.println("inside beforeMethod Aspect");
    }
}


/*
Type sof pointcut:
1. "execution": matches a particular method in a particular class. Access-modifier is optional to mention, and works on all 4 access-modifiers.
    Return type  with classpath of method with parameter type is mandatory.
    Use of wildcards in pointcut:
      a. "*" : matches any single element. Example:
        A. "*" at return type to match any return type;
        B. * com.class.path.Class.*(String) :method in Class class having 1 param of type String returning any ret type;
        C. String com.class.path.Class.methodName(*) : match given method of given return type having single param of any type.

      b. ".." : matches 0 or more element. Example:
        A. retType com.class.path.Class(..) : matches methods in Class class with given return type having any count(>=0) of arguments of any type.
        B. retType com.class..methodName()  : matches method "methodName" with no param and given return type in package "com.class" and its subpackage(s)
        C. String com.class..*()   : matches any method with return type "String" having no param in package "com.class" and its subpackage(s)

2. "within" : matches all the methods within mentioned package or class. We can do it with executions also. But this should be preferred in such cases.
    Wildcards: Example:
        A. @Before("within(com.class.path..*)")    : matches all the methods withing package "com.class.path" and its subpackages.
        B. @Before("within(com.class.path.Class)")  : matches all the methods withing class "com.class.path.Class".
 */