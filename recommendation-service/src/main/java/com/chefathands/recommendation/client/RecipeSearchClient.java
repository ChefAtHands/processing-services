package com.chefathands.recommendation.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RecipeSearchClient {
    private static final Logger logger = LoggerFactory.getLogger(RecipeSearchClient.class);

    private final RestTemplate restTemplate;
    private final String recipeSearchUrl;

    public RecipeSearchClient(RestTemplate restTemplate,
                              @Value("${services.recipe-search.url:http://localhost:8085}") String recipeSearchUrl) {
        this.restTemplate = restTemplate;
        this.recipeSearchUrl = recipeSearchUrl;
    }

    public RecipeSearchResponse search(RecipeSearchRequest request) {
        String url = recipeSearchUrl + "/api/recipes/search";
        try {
            logger.debug("Calling external recipe search: {}", url);
            HttpEntity<RecipeSearchRequest> entity = new HttpEntity<>(request);
            ResponseEntity<RecipeSearchResponse> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                RecipeSearchResponse.class
            );
            return response.getBody();
        } catch (Exception e) {
            logger.error("Error calling recipe-search-service: {}", e.getMessage());
            return null;
        }
    }
}
