package com.chefathands.recommendation.controller;

import com.chefathands.recommendation.dto.RecipeFilters;
import com.chefathands.recommendation.dto.RecommendationRequest;
import com.chefathands.recommendation.dto.RecommendationResponse;
import com.chefathands.recommendation.model.Recipe;
import com.chefathands.recommendation.service.RecommendationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    /**
     * GET /api/recommendations
     * 
     * Search recipes using user's saved ingredients
     * 
     * Example: GET /api/recommendations?userID=123&number=10&offset=0&diet=vegetarian&type=dessert
     */
    @GetMapping
    public ResponseEntity<RecommendationResponse> getRecommendations(
            @RequestParam("userID") @Min(1) Long userId,
            @RequestParam(value = "number", defaultValue = "10") @Min(1) Integer number,
            @RequestParam(value = "offset", defaultValue = "0") @Min(0) Integer offset,
            @RequestParam(value = "diet", required = false) String diet,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "minCarbs", required = false) Integer minCarbs,
            @RequestParam(value = "maxCarbs", required = false) Integer maxCarbs,
            @RequestParam(value = "minProtein", required = false) Integer minProtein,
            @RequestParam(value = "maxProtein", required = false) Integer maxProtein,
            @RequestParam(value = "minFat", required = false) Integer minFat,
            @RequestParam(value = "maxFat", required = false) Integer maxFat,
            @RequestParam(value = "minCalories", required = false) Integer minCalories,
            @RequestParam(value = "maxCalories", required = false) Integer maxCalories) {

        // Build filters object from query params
        RecipeFilters filters = new RecipeFilters();
        filters.setNumber(number);
        filters.setOffset(offset);
        filters.setDiet(diet);
        filters.setType(type);
        filters.setMinCarbs(minCarbs);
        filters.setMaxCarbs(maxCarbs);
        filters.setMinProtein(minProtein);
        filters.setMaxProtein(maxProtein);
        filters.setMinFat(minFat);
        filters.setMaxFat(maxFat);
        filters.setMinCalories(minCalories);
        filters.setMaxCalories(maxCalories);

        // Get recommendations (filtering and pagination handled by external API)
        RecommendationResponse response = recommendationService.getRecommendationsForUser(userId, filters);
        
        return ResponseEntity.ok(response);
    }

    /**
     * POST /api/recommendations
     * 
     * Search recipes using ingredients provided in request body
     * 
     * Example: POST /api/recommendations?userID=123&number=10&offset=0&diet=vegan&type=main course
     * Body: { "ingredients": [{"name": "tomato", "quantity": 4, "ingredientID": 1}, ...] }
     */
    @PostMapping
    public ResponseEntity<RecommendationResponse> getRecommendationsByIngredients(
            @RequestParam("userID") @Min(1) Long userId,
            @RequestParam(value = "number", defaultValue = "10") @Min(1) Integer number,
            @RequestParam(value = "offset", defaultValue = "0") @Min(0) Integer offset,
            @RequestParam(value = "diet", required = false) String diet,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "minCarbs", required = false) Integer minCarbs,
            @RequestParam(value = "maxCarbs", required = false) Integer maxCarbs,
            @RequestParam(value = "minProtein", required = false) Integer minProtein,
            @RequestParam(value = "maxProtein", required = false) Integer maxProtein,
            @RequestParam(value = "minFat", required = false) Integer minFat,
            @RequestParam(value = "maxFat", required = false) Integer maxFat,
            @RequestParam(value = "minCalories", required = false) Integer minCalories,
            @RequestParam(value = "maxCalories", required = false) Integer maxCalories,
            @Valid @RequestBody RecommendationRequest request) {

        // Build filters object from query params
        RecipeFilters filters = new RecipeFilters();
        filters.setNumber(number);
        filters.setOffset(offset);
        filters.setDiet(diet);
        filters.setType(type);
        filters.setMinCarbs(minCarbs);
        filters.setMaxCarbs(maxCarbs);
        filters.setMinProtein(minProtein);
        filters.setMaxProtein(maxProtein);
        filters.setMinFat(minFat);
        filters.setMaxFat(maxFat);
        filters.setMinCalories(minCalories);
        filters.setMaxCalories(maxCalories);

        // Get recommendations based on provided ingredients (filtering and pagination handled by external API)
        RecommendationResponse response = recommendationService.getRecommendationsByIngredients(
            request.getIngredients(), 
            filters
        );
        
        return ResponseEntity.ok(response);
    }
}
