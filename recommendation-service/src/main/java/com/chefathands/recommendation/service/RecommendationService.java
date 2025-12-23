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

    // ...rest of the code stays the same...
    
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
}package com.chefathands.recipesearch.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.chefathands.recipesearch.client.SpoonacularClient;
import com.chefathands.recipesearch.dto.RecipeSearchRequest;
import com.chefathands.recipesearch.dto.RecipeSearchResponse;
import com.chefathands.recipesearch.model.Recipe;
import com.chefathands.recipesearch.model.RecipeCache;
import com.chefathands.recipesearch.repository.RecipeCacheRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class RecipeSearchService {

    private static final Logger logger = LoggerFactory.getLogger(RecipeSearchService.class);
    private static final int CACHE_EXPIRY_HOURS = 24;

    private final SpoonacularClient spoonacularClient;
    private final RecipeCacheRepository recipeCacheRepository;
    private final ObjectMapper objectMapper;

    public RecipeSearchService(SpoonacularClient spoonacularClient, 
                              RecipeCacheRepository recipeCacheRepository,
                              ObjectMapper objectMapper) {
        this.spoonacularClient = spoonacularClient;
        this.recipeCacheRepository = recipeCacheRepository;
        this.objectMapper = objectMapper;
    }

    /**
     * Search recipes by ingredients with caching
     */
    public RecipeSearchResponse searchRecipes(RecipeSearchRequest request) {
        logger.info("Searching recipes with ingredients: {}", request.getIngredients());

        // Create a cache key from the search parameters
        String cacheKey = generateCacheKey(request);
        logger.debug("Generated cache key: {}", cacheKey);

        // Check if we have cached results for this exact search
        LocalDateTime cacheExpiry = LocalDateTime.now().minusHours(CACHE_EXPIRY_HOURS);
        
        // TODO: Check search cache table here (not implemented yet)
        // For now, we'll check if individual recipes are cached
        
        try {
            // Call Spoonacular API
            JsonNode response = spoonacularClient.searchRecipesByIngredients(request);
            
            // Parse response
            List<Recipe> recipes = new ArrayList<>();
            JsonNode resultsNode = response.get("results");
            
            if (resultsNode != null && resultsNode.isArray()) {
                for (JsonNode node : resultsNode) {
                    try {
                        logger.debug("Processing recipe node: {}", node.toString());
                        
                        Long recipeId = node.get("id").asLong();
                        
                        // Check if this specific recipe is already cached
                        Optional<RecipeCache> cachedRecipe = recipeCacheRepository
                            .findBySpoonacularIdAndCachedAtAfter(recipeId, cacheExpiry);
                        
                        Recipe recipe;
                        if (cachedRecipe.isPresent()) {
                            logger.info("Recipe {} found in cache", recipeId);
                            recipe = objectMapper.readValue(cachedRecipe.get().getRecipeData(), Recipe.class);
                        } else {
                            logger.info("Recipe {} not in cache, parsing from API response", recipeId);
                            recipe = objectMapper.treeToValue(node, Recipe.class);
                            // Cache this recipe
                            cacheRecipe(node);
                        }
                        
                        recipes.add(recipe);
                    } catch (Exception e) {
                        logger.error("Error parsing recipe node", e);
                    }
                }
            }

            Integer totalResults = response.has("totalResults") ? response.get("totalResults").asInt() : recipes.size();
            Integer offset = response.has("offset") ? response.get("offset").asInt() : 0;
            Integer number = response.has("number") ? response.get("number").asInt() : recipes.size();

            logger.info("Found {} recipes out of {} total results", recipes.size(), totalResults);

            return new RecipeSearchResponse(recipes, totalResults, offset, number);

        } catch (Exception e) {
            logger.error("Error searching recipes", e);
            throw new RuntimeException("Failed to search recipes: " + e.getMessage());
        }
    }

    /**
     * Generate a unique cache key based on search parameters
     */
    private String generateCacheKey(RecipeSearchRequest request) {
        StringBuilder key = new StringBuilder();
        
        // Sort ingredients to ensure consistent key
        List<String> ingredients = new ArrayList<>(request.getIngredients());
        ingredients.sort(String::compareTo);
        key.append("ingredients:").append(String.join(",", ingredients));
        
        if (request.getType() != null) key.append("|type:").append(request.getType());
        if (request.getDiet() != null) key.append("|diet:").append(request.getDiet());
        if (request.getMinProtein() != null) key.append("|minProtein:").append(request.getMinProtein());
        if (request.getMaxProtein() != null) key.append("|maxProtein:").append(request.getMaxProtein());
        if (request.getMinCarbs() != null) key.append("|minCarbs:").append(request.getMinCarbs());
        if (request.getMaxCarbs() != null) key.append("|maxCarbs:").append(request.getMaxCarbs());
        if (request.getMinCalories() != null) key.append("|minCalories:").append(request.getMinCalories());
        if (request.getMaxCalories() != null) key.append("|maxCalories:").append(request.getMaxCalories());
        if (request.getNumber() != null) key.append("|number:").append(request.getNumber());
        if (request.getOffset() != null) key.append("|offset:").append(request.getOffset());
        
        return key.toString();
    }

    /**
     * Get recipe details by ID (check cache first)
     */
    public Recipe getRecipeById(Long recipeId) {
        logger.info("Fetching recipe with ID: {}", recipeId);

        // Check cache first
        Optional<RecipeCache> cachedRecipe = recipeCacheRepository.findBySpoonacularIdAndCachedAtAfter(
                recipeId, 
                LocalDateTime.now().minusHours(CACHE_EXPIRY_HOURS)
        );

        if (cachedRecipe.isPresent()) {
            logger.info("Recipe found in cache: {}", recipeId);
            try {
                return objectMapper.readValue(cachedRecipe.get().getRecipeData(), Recipe.class);
            } catch (Exception e) {
                logger.error("Error parsing cached recipe", e);
            }
        }

        // Fetch from API if not in cache
        Recipe recipe = spoonacularClient.getRecipeDetails(recipeId);
        
        // Convert Recipe to JsonNode for caching
        try {
            JsonNode recipeNode = objectMapper.valueToTree(recipe);
            cacheRecipe(recipeNode);
        } catch (Exception e) {
            logger.error("Error converting recipe to JSON for caching", e);
        }
        
        return recipe;
    }

    /**
     * Cache recipe data
     */
    private void cacheRecipe(JsonNode recipeNode) {
        try {
            Long spoonacularId = recipeNode.get("id").asLong();
            String recipeJson = recipeNode.toString();
            
            // Extract title from the JSON
            String title = recipeNode.has("title") ? recipeNode.get("title").asText() : "Unknown Recipe";
            
            RecipeCache cache = new RecipeCache(spoonacularId, recipeJson, LocalDateTime.now());
            cache.setTitle(title);  // Set the title before saving
            
            recipeCacheRepository.save(cache);
            logger.info("Successfully cached recipe: {} (ID: {})", title, spoonacularId);
        } catch (Exception e) {
            logger.error("Error caching recipe: {}", recipeNode.get("id").asLong(), e);
        }
    }   
}