package com.chefathands.recommendation.controller;

import com.chefathands.recommendation.dto.RecipeFilters;
import com.chefathands.recommendation.dto.RecommendationRequest;
import com.chefathands.recommendation.dto.RecommendationResponse;
import com.chefathands.recommendation.service.RecommendationService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    // -----------------------------
    // GET — Recommendations by user
    // -----------------------------
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
            @RequestParam(value = "maxCalories", required = false) Integer maxCalories
    ) {

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

        RecommendationResponse response =
                recommendationService.getRecommendationsForUser(userId, filters);

        return ResponseEntity.ok(response);
    }

    // -----------------------------------------
    // POST — Recommendations by provided ingredients
    // -----------------------------------------
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

            @Valid @RequestBody RecommendationRequest request
    ) {

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

        RecommendationResponse response =
                recommendationService.getRecommendationsByIngredients(
                        request.getIngredients(),
                        filters
                );

        return ResponseEntity.ok(response);
    }
}