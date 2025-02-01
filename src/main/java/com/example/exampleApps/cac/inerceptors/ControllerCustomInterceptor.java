package com.example.exampleApps.cac.inerceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class ControllerCustomInterceptor implements HandlerInterceptor {  // If we have implemented HandlerInterceptor interface, we need to provide body to these 3 default methods.
    @Override   // Before the intercepted controller gets invoked, this body will get executed
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {   // If this returns false, then post and after methods will not get invoked for this interceptor.
//        return HandlerInterceptor.super.preHandle(request, response, handler);
        System.out.println("Inside preHandle method");
        return true;
    }

    @Override   // Invoked when controller is removed from the call stack
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
        System.out.println("Inside postHandle method");
    }

    @Override   // Invoked after postHandle method
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
        System.out.println("Inside afterCompletion method");
    }
}
