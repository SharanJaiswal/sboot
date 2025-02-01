package com.example.exampleApps.cac.inerceptors;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
@Aspect
public class AnnotationBasedCustomInterceptor {
    @Around("@annotation(com.example.exampleApps.cac.inerceptors.InterceptorAnnotation)")
    public void invoke(ProceedingJoinPoint joinPoint) throws Throwable {

        System.out.println("Do something before actual method.");
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        if(method.isAnnotationPresent(InterceptorAnnotation.class)) {
            InterceptorAnnotation annotation = method.getAnnotation(InterceptorAnnotation.class);
            System.out.println("Name for annotation" + annotation.name());
        }

        joinPoint.proceed();

        System.out.println("Do something after actual method");
    }
}
