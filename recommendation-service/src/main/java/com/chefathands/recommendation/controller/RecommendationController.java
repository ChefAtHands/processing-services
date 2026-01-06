package com.chefathands.recommendation.controller;

import com.chefathands.recommendation.dto.RecipeFilters;
import com.chefathands.recommendation.dto.RecommendationRequest;
import com.chefathands.recommendation.dto.RecommendationResponse;
import com.chefathands.recommendation.service.RecommendationService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

<<<<<<< HEAD
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import java.util.List;

=======
>>>>>>> cabf8ff (popravljen za delovanje frontenda)
@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

<<<<<<< HEAD
    /**
     * GET /api/recommendations
     * 
     * Search recipes using user's saved ingredients
     * 
     * Example: GET /api/recommendations?userID=123&number=10&offset=0&diet=vegetarian&type=dessert
     */
=======
    // -----------------------------
    // GET — Recommendations by user
    // -----------------------------
>>>>>>> cabf8ff (popravljen za delovanje frontenda)
    @GetMapping
    public ResponseEntity<RecommendationResponse> getRecommendations(
            @RequestParam("userID") @Min(1) Long userId,
            @RequestParam(value = "number", defaultValue = "10") @Min(1) Integer number,
            @RequestParam(value = "offset", defaultValue = "0") @Min(0) Integer offset,
<<<<<<< HEAD
            @RequestParam(value = "diet", required = false) String diet,
            @RequestParam(value = "type", required = false) String type,
=======

            @RequestParam(value = "diet", required = false) String diet,
            @RequestParam(value = "type", required = false) String type,

>>>>>>> cabf8ff (popravljen za delovanje frontenda)
            @RequestParam(value = "minCarbs", required = false) Integer minCarbs,
            @RequestParam(value = "maxCarbs", required = false) Integer maxCarbs,
            @RequestParam(value = "minProtein", required = false) Integer minProtein,
            @RequestParam(value = "maxProtein", required = false) Integer maxProtein,
            @RequestParam(value = "minFat", required = false) Integer minFat,
            @RequestParam(value = "maxFat", required = false) Integer maxFat,
            @RequestParam(value = "minCalories", required = false) Integer minCalories,
<<<<<<< HEAD
            @RequestParam(value = "maxCalories", required = false) Integer maxCalories) {
=======
            @RequestParam(value = "maxCalories", required = false) Integer maxCalories
    ) {
>>>>>>> cabf8ff (popravljen za delovanje frontenda)

        RecipeFilters filters = new RecipeFilters();
        filters.setNumber(number);
        filters.setOffset(offset);
<<<<<<< HEAD
        filters.setDiet(diet);
        filters.setType(type);
=======

        filters.setDiet(diet);
        filters.setType(type);

>>>>>>> cabf8ff (popravljen za delovanje frontenda)
        filters.setMinCarbs(minCarbs);
        filters.setMaxCarbs(maxCarbs);
        filters.setMinProtein(minProtein);
        filters.setMaxProtein(maxProtein);
        filters.setMinFat(minFat);
        filters.setMaxFat(maxFat);
        filters.setMinCalories(minCalories);
        filters.setMaxCalories(maxCalories);

<<<<<<< HEAD
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
=======
        RecommendationResponse response =
                recommendationService.getRecommendationsForUser(userId, filters);

        return ResponseEntity.ok(response);
    }

    // -----------------------------------------
    // POST — Recommendations by provided ingredients
    // -----------------------------------------
>>>>>>> cabf8ff (popravljen za delovanje frontenda)
    @PostMapping
    public ResponseEntity<RecommendationResponse> getRecommendationsByIngredients(
            @RequestParam("userID") @Min(1) Long userId,
            @RequestParam(value = "number", defaultValue = "10") @Min(1) Integer number,
            @RequestParam(value = "offset", defaultValue = "0") @Min(0) Integer offset,
<<<<<<< HEAD
            @RequestParam(value = "diet", required = false) String diet,
            @RequestParam(value = "type", required = false) String type,
=======

            @RequestParam(value = "diet", required = false) String diet,
            @RequestParam(value = "type", required = false) String type,

>>>>>>> cabf8ff (popravljen za delovanje frontenda)
            @RequestParam(value = "minCarbs", required = false) Integer minCarbs,
            @RequestParam(value = "maxCarbs", required = false) Integer maxCarbs,
            @RequestParam(value = "minProtein", required = false) Integer minProtein,
            @RequestParam(value = "maxProtein", required = false) Integer maxProtein,
            @RequestParam(value = "minFat", required = false) Integer minFat,
            @RequestParam(value = "maxFat", required = false) Integer maxFat,
            @RequestParam(value = "minCalories", required = false) Integer minCalories,
            @RequestParam(value = "maxCalories", required = false) Integer maxCalories,
<<<<<<< HEAD
            @Valid @RequestBody RecommendationRequest request) {
=======
>>>>>>> cabf8ff (popravljen za delovanje frontenda)

            @Valid @RequestBody RecommendationRequest request
    ) {

        RecipeFilters filters = new RecipeFilters();
        filters.setNumber(number);
        filters.setOffset(offset);
<<<<<<< HEAD
        filters.setDiet(diet);
        filters.setType(type);
=======

        filters.setDiet(diet);
        filters.setType(type);

>>>>>>> cabf8ff (popravljen za delovanje frontenda)
        filters.setMinCarbs(minCarbs);
        filters.setMaxCarbs(maxCarbs);
        filters.setMinProtein(minProtein);
        filters.setMaxProtein(maxProtein);
        filters.setMinFat(minFat);
        filters.setMaxFat(maxFat);
        filters.setMinCalories(minCalories);
        filters.setMaxCalories(maxCalories);

<<<<<<< HEAD
        // Get recommendations based on provided ingredients (filtering and pagination handled by external API)
        RecommendationResponse response = recommendationService.getRecommendationsByIngredients(
            request.getIngredients(), 
            filters
        );
        
=======
        RecommendationResponse response =
                recommendationService.getRecommendationsByIngredients(
                        request.getIngredients(),
                        filters
                );

>>>>>>> cabf8ff (popravljen za delovanje frontenda)
        return ResponseEntity.ok(response);
    }
}