package com.chefathands.recommendation.service;

import com.chefathands.recommendation.client.IngredientServiceClient;
import com.chefathands.recommendation.client.RecipeSearchClient;
import com.chefathands.recommendation.client.RecipeSearchRequest;
import com.chefathands.recommendation.client.RecipeSearchResponse;
import com.chefathands.recommendation.dto.IngredientRequest;
import com.chefathands.recommendation.dto.RecipeFilters;
import com.chefathands.recommendation.dto.RecommendationResponse;
import com.chefathands.recommendation.dto.UserIngredientDTO;
import com.chefathands.recommendation.model.Recipe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecommendationService {
    
    private static final Logger logger = LoggerFactory.getLogger(RecommendationService.class);
    
    private final IngredientServiceClient ingredientServiceClient;
    private final RecipeSearchClient recipeSearchClient;
    private final RestTemplate restTemplate;
    
    @Value("${ingredient.service.url:http://localhost:8081}")
    private String ingredientServiceUrl;
    
    public RecommendationService(IngredientServiceClient ingredientServiceClient,
                                 RecipeSearchClient recipeSearchClient,
                                 RestTemplate restTemplate) {
        this.ingredientServiceClient = ingredientServiceClient;
        this.recipeSearchClient = recipeSearchClient;
        this.restTemplate = restTemplate;
    }

    public RecommendationResponse getRecommendationsForUser(Long userId, RecipeFilters filters) {
        logger.info("Getting recommendations for user {}", userId);
        
        // 1. Fetch user's saved ingredients from ingredient-service
        List<UserIngredientDTO> userIngredients = ingredientServiceClient.getUserIngredients(userId);
        logger.debug("User {} has {} saved ingredients", userId, userIngredients.size());
        
        if (userIngredients.isEmpty()) {
            logger.info("User {} has no ingredients, returning empty result", userId);
            return new RecommendationResponse(new ArrayList<>(), 0, 
                filters.getOffset() != null ? filters.getOffset() : 0, 
                filters.getNumber() != null ? filters.getNumber() : 10);
        }
        
        // 2. Fetch ingredient names from ingredient-service
        List<String> ingredientNames = new ArrayList<>();
        for (UserIngredientDTO userIngredient : userIngredients) {
            try {
                String url = ingredientServiceUrl + "/api/ingredients/" + userIngredient.getIngredientId();
                logger.debug("Fetching ingredient name from: {}", url);
                
                // Call ingredient-service to get ingredient details
                IngredientDTO ingredient = restTemplate.getForObject(url, IngredientDTO.class);
                if (ingredient != null && ingredient.getName() != null) {
                    ingredientNames.add(ingredient.getName());
                    logger.debug("Added ingredient: {}", ingredient.getName());
                }
            } catch (Exception e) {
                logger.error("Error fetching ingredient {}: {}", userIngredient.getIngredientId(), e.getMessage());
            }
        }
        
        if (ingredientNames.isEmpty()) {
            logger.info("Could not resolve ingredient names for user {}", userId);
            return new RecommendationResponse(new ArrayList<>(), 0,
                filters.getOffset() != null ? filters.getOffset() : 0,
                filters.getNumber() != null ? filters.getNumber() : 10);
        }
        
        logger.info("Resolved ingredient names: {}", ingredientNames);
        
        // 3. Call recipe-search-service with ingredient names
        RecipeSearchRequest searchRequest = new RecipeSearchRequest();
        searchRequest.setIngredients(ingredientNames);
        searchRequest.setNumber(filters.getNumber() != null ? filters.getNumber() : 10);
        searchRequest.setOffset(filters.getOffset() != null ? filters.getOffset() : 0);
        searchRequest.setDiet(filters.getDiet());
        searchRequest.setType(filters.getType());
        searchRequest.setMinProtein(filters.getMinProtein());
        searchRequest.setMaxProtein(filters.getMaxProtein());
        searchRequest.setMinCarbs(filters.getMinCarbs());
        searchRequest.setMaxCarbs(filters.getMaxCarbs());
        searchRequest.setMinCalories(filters.getMinCalories());
        searchRequest.setMaxCalories(filters.getMaxCalories());
        searchRequest.setMinFat(filters.getMinFat());
        searchRequest.setMaxFat(filters.getMaxFat());

        RecipeSearchResponse externalResponse = recipeSearchClient.search(searchRequest);
        if (externalResponse == null) {
            return new RecommendationResponse(new ArrayList<>(), 0,
                filters.getOffset() != null ? filters.getOffset() : 0,
                filters.getNumber() != null ? filters.getNumber() : 10);
        }

        List<Recipe> results = externalResponse.getResults();
        Integer total = externalResponse.getTotalResults() != null ? externalResponse.getTotalResults() : (results != null ? results.size() : 0);
        Integer offset = externalResponse.getOffset() != null ? externalResponse.getOffset() : (filters.getOffset() != null ? filters.getOffset() : 0);
        Integer number = externalResponse.getNumber() != null ? externalResponse.getNumber() : (filters.getNumber() != null ? filters.getNumber() : 10);

        return new RecommendationResponse(results != null ? results : new ArrayList<>(), total, offset, number);
    }
    
    public RecommendationResponse getRecommendationsByIngredients(List<IngredientRequest> ingredients, 
                                                                   RecipeFilters filters) {
        logger.info("Getting recommendations for {} provided ingredients",
            ingredients != null ? ingredients.size() : 0);

        List<String> ingredientNames = ingredients.stream()
            .map(IngredientRequest::getName)
            .collect(Collectors.toList());

        if (ingredientNames.isEmpty()) {
            logger.info("No ingredients provided, returning empty result");
            return new RecommendationResponse(new ArrayList<>(), 0,
                filters.getOffset() != null ? filters.getOffset() : 0,
                filters.getNumber() != null ? filters.getNumber() : 10);
        }

        RecipeSearchRequest searchRequest = new RecipeSearchRequest();
        searchRequest.setIngredients(ingredientNames);
        searchRequest.setNumber(filters.getNumber() != null ? filters.getNumber() : 10);
        searchRequest.setOffset(filters.getOffset() != null ? filters.getOffset() : 0);
        searchRequest.setDiet(filters.getDiet());
        searchRequest.setType(filters.getType());
        searchRequest.setMinProtein(filters.getMinProtein());
        searchRequest.setMaxProtein(filters.getMaxProtein());
        searchRequest.setMinCarbs(filters.getMinCarbs());
        searchRequest.setMaxCarbs(filters.getMaxCarbs());
        searchRequest.setMinCalories(filters.getMinCalories());
        searchRequest.setMaxCalories(filters.getMaxCalories());
        searchRequest.setMinFat(filters.getMinFat());
        searchRequest.setMaxFat(filters.getMaxFat());

        RecipeSearchResponse externalResponse = recipeSearchClient.search(searchRequest);
        if (externalResponse == null) {
            return new RecommendationResponse(new ArrayList<>(), 0,
                filters.getOffset() != null ? filters.getOffset() : 0,
                filters.getNumber() != null ? filters.getNumber() : 10);
        }

        List<Recipe> results = externalResponse.getResults();
        Integer total = externalResponse.getTotalResults() != null ? externalResponse.getTotalResults() : (results != null ? results.size() : 0);
        Integer offset = externalResponse.getOffset() != null ? externalResponse.getOffset() : (filters.getOffset() != null ? filters.getOffset() : 0);
        Integer number = externalResponse.getNumber() != null ? externalResponse.getNumber() : (filters.getNumber() != null ? filters.getNumber() : 10);

        return new RecommendationResponse(results != null ? results : new ArrayList<>(), total, offset, number);
    }
    
    // Helper DTO class
    public static class IngredientDTO {
        private Integer ingredientId;
        private String name;
        private String category;
        
        public Integer getIngredientId() { return ingredientId; }
        public void setIngredientId(Integer ingredientId) { this.ingredientId = ingredientId; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
    }
}