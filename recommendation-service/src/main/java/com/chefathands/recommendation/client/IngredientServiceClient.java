package com.chefathands.recommendation.client;

import com.chefathands.recommendation.dto.UserIngredientDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Component
public class IngredientServiceClient {
    
    private static final Logger logger = LoggerFactory.getLogger(IngredientServiceClient.class);
    
    private final RestTemplate restTemplate;
    private final String ingredientServiceUrl;
    
    public IngredientServiceClient(
            RestTemplate restTemplate,
            @Value("${services.ingredient.url:http://localhost:8081}") String ingredientServiceUrl) {
        this.restTemplate = restTemplate;
        this.ingredientServiceUrl = ingredientServiceUrl;
    }
    
    /**
     * Fetch user's saved ingredients from ingredient-service
     * 
     * @param userId User ID
     * @return List of user ingredients
     */
    public List<UserIngredientDTO> getUserIngredients(Long userId) {
        String url = ingredientServiceUrl + "/api/users/" + userId + "/ingredients";
        
        try {
            logger.debug("Fetching ingredients for user {} from {}", userId, url);
            
            ResponseEntity<List<UserIngredientDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<UserIngredientDTO>>() {}
            );
            
            List<UserIngredientDTO> ingredients = response.getBody();
            logger.debug("Fetched {} ingredients for user {}", 
                ingredients != null ? ingredients.size() : 0, userId);
            
            return ingredients != null ? ingredients : Collections.emptyList();
            
        } catch (Exception e) {
            logger.error("Error fetching ingredients for user {}: {}", userId, e.getMessage());
            // Return empty list instead of propagating exception
            // This allows recommendation service to continue with empty ingredient list
            return Collections.emptyList();
        }
    }
}
