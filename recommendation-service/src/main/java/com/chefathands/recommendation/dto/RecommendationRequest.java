package com.chefathands.recommendation.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

public class RecommendationRequest {
    
    @NotEmpty(message = "Ingredients list cannot be empty")
    @Valid
    private List<IngredientRequest> ingredients;

    public RecommendationRequest() {}

    public RecommendationRequest(List<IngredientRequest> ingredients) {
        this.ingredients = ingredients;
    }

    public List<IngredientRequest> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<IngredientRequest> ingredients) {
        this.ingredients = ingredients;
    }
}
