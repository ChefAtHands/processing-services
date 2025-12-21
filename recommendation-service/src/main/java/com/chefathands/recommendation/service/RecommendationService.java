package com.chefathands.recommendation.service;

import com.chefathands.recommendation.client.IngredientServiceClient;
import com.chefathands.recommendation.dto.IngredientRequest;
import com.chefathands.recommendation.dto.RecipeFilters;
import com.chefathands.recommendation.dto.RecommendationResponse;
import com.chefathands.recommendation.dto.UserIngredientDTO;
import com.chefathands.recommendation.model.Recipe;
import com.chefathands.recommendation.model.RecipeIngredient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecommendationService {
    
    private static final Logger logger = LoggerFactory.getLogger(RecommendationService.class);
    
    private final IngredientServiceClient ingredientServiceClient;
    private final com.chefathands.recommendation.client.RecipeSearchClient recipeSearchClient;
    
    public RecommendationService(IngredientServiceClient ingredientServiceClient,
                                 com.chefathands.recommendation.client.RecipeSearchClient recipeSearchClient) {
        this.ingredientServiceClient = ingredientServiceClient;
        this.recipeSearchClient = recipeSearchClient;
    }

    /**
     * Get recipe recommendations based on user's saved ingredients
     * 
     * @param userId User ID
     * @param filters Recipe filters (diet, type, nutritional values, pagination)
     * @return RecommendationResponse with recipes and metadata
     */
    public RecommendationResponse getRecommendationsForUser(Long userId, RecipeFilters filters) {
        logger.info("Getting recommendations for user {}", userId);
        
        // 1. Fetch user's saved ingredients from ingredient-service
        List<UserIngredientDTO> userIngredients = ingredientServiceClient.getUserIngredients(userId);
        logger.debug("User {} has {} saved ingredients", userId, userIngredients.size());
        
        // 2. Convert to ingredient names for external API
        List<String> ingredientNames = userIngredients.stream()
            .map(ui -> "ingredient_" + ui.getIngredientId()) // TODO: Map IDs to actual names
            .collect(Collectors.toList());
        
        if (ingredientNames.isEmpty()) {
            logger.info("User {} has no ingredients, returning empty result", userId);
            return new RecommendationResponse(new ArrayList<>(), 0, 
                filters.getOffset() != null ? filters.getOffset() : 0, 
                filters.getNumber() != null ? filters.getNumber() : 10);
        }
        
        // 3. Call external API service with filters (handles filtering and pagination)
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
            // Fallback to empty
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

    /**
     * Get recipe recommendations based on provided ingredients
     * 
     * @param ingredients List of ingredients with quantities
     * @param filters Recipe filters (diet, type, nutritional values, pagination)
     * @return RecommendationResponse with recipes and metadata
     */
    public RecommendationResponse getRecommendationsByIngredients(List<IngredientRequest> ingredients, 
                                                                   RecipeFilters filters) {
        logger.info("Getting recommendations for {} provided ingredients",
            ingredients != null ? ingredients.size() : 0);

        // Convert to ingredient names for external API
        List<String> ingredientNames = ingredients.stream()
            .map(IngredientRequest::getName)
            .collect(Collectors.toList());

        if (ingredientNames.isEmpty()) {
            logger.info("No ingredients provided, returning empty result");
            return new RecommendationResponse(new ArrayList<>(), 0,
                filters.getOffset() != null ? filters.getOffset() : 0,
                filters.getNumber() != null ? filters.getNumber() : 10);
        }

        // Build request for external recipe-search-service
        com.chefathands.recommendation.client.RecipeSearchRequest searchRequest = new com.chefathands.recommendation.client.RecipeSearchRequest();
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

        com.chefathands.recommendation.client.RecipeSearchResponse externalResponse = recipeSearchClient.search(searchRequest);
        if (externalResponse == null) {
            // Fallback to empty
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

    // Mock data - replace with actual external API calls
    private List<Recipe> getMockRecipes() {
        List<Recipe> recipes = new ArrayList<>();

        Recipe recipe1 = new Recipe(1L, "Tomato Pasta", "Delicious pasta with tomatoes", "dinner");
        recipe1.setCalories(450);
        recipe1.setProtein(15);
        recipe1.setCarbs(60);
        recipe1.setFat(12);
        recipe1.setPreparationTime(30);
        recipe1.setServings(2);
        recipe1.setIngredients(Arrays.asList(
            new RecipeIngredient(1L, "tomato", 4, "pcs"),
            new RecipeIngredient(2L, "pasta", 200, "g")
        ));
        recipe1.setInstructions(Arrays.asList(
            "Boil water and cook pasta",
            "Prepare tomato sauce",
            "Mix and serve"
        ));
        recipes.add(recipe1);

        Recipe recipe2 = new Recipe(2L, "Chocolate Cake", "Rich chocolate dessert", "dessert");
        recipe2.setCalories(550);
        recipe2.setProtein(8);
        recipe2.setCarbs(75);
        recipe2.setFat(25);
        recipe2.setPreparationTime(60);
        recipe2.setServings(8);
        recipe2.setIngredients(Arrays.asList(
            new RecipeIngredient(5L, "milk", 200, "ml"),
            new RecipeIngredient(6L, "chocolate", 100, "g"),
            new RecipeIngredient(7L, "flour", 250, "g")
        ));
        recipe2.setInstructions(Arrays.asList(
            "Mix dry ingredients",
            "Add wet ingredients",
            "Bake at 180C for 45 minutes"
        ));
        recipes.add(recipe2);

        Recipe recipe3 = new Recipe(3L, "Meat Stew", "Hearty meat stew", "dinner");
        recipe3.setCalories(600);
        recipe3.setProtein(45);
        recipe3.setCarbs(30);
        recipe3.setFat(35);
        recipe3.setPreparationTime(120);
        recipe3.setServings(4);
        recipe3.setIngredients(Arrays.asList(
            new RecipeIngredient(3L, "meat", 500, "g"),
            new RecipeIngredient(1L, "tomato", 2, "pcs"),
            new RecipeIngredient(8L, "onion", 1, "pcs")
        ));
        recipe3.setInstructions(Arrays.asList(
            "Brown the meat",
            "Add vegetables and liquid",
            "Simmer for 2 hours"
        ));
        recipes.add(recipe3);

        Recipe recipe4 = new Recipe(4L, "Vegan Breakfast Bowl", "Healthy morning bowl", "breakfast");
        recipe4.setCalories(350);
        recipe4.setProtein(12);
        recipe4.setCarbs(55);
        recipe4.setFat(8);
        recipe4.setPreparationTime(15);
        recipe4.setServings(1);
        recipe4.setIngredients(Arrays.asList(
            new RecipeIngredient(9L, "oats", 50, "g"),
            new RecipeIngredient(10L, "banana", 1, "pcs"),
            new RecipeIngredient(5L, "milk", 150, "ml")
        ));
        recipe4.setInstructions(Arrays.asList(
            "Cook oats with milk",
            "Top with sliced banana",
            "Serve warm"
        ));
        recipes.add(recipe4);

        return recipes;
    }
}
