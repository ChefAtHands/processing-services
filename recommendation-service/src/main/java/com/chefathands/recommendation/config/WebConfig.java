package com.chefathands.recommendation.config;

import com.chefathands.recommendation.interceptor.RecommendationInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final RecommendationInterceptor recommendationInterceptor;

    public WebConfig(RecommendationInterceptor recommendationInterceptor) {
        this.recommendationInterceptor = recommendationInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(recommendationInterceptor)
                .addPathPatterns("/api/recommendations/**");
    }
}
