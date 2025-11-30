package com.chefathands.recommendation.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class RecommendationInterceptor implements HandlerInterceptor {

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, 
                                Object handler, Exception ex) throws Exception {
        String method = request.getMethod();
        String uri = request.getRequestURI();
        
        if ((method.equals("GET") || method.equals("POST")) && uri.contains("/recommendations")) {
            System.out.println("request received");
            //log logic can be added here
        }
    }
}
