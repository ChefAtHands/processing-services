package com.chefathands.recommendation.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class IngredientRequest {
    
    @NotBlank(message = "Ingredient name is required")
    private String name;
    
    @NotNull(message = "Ingredient ID is required")
    private Long ingredientID;
    
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    public IngredientRequest() {}

    public IngredientRequest(String name, Long ingredientID, Integer quantity) {
        this.name = name;
        this.ingredientID = ingredientID;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getIngredientID() {
        return ingredientID;
    }

    public void setIngredientID(Long ingredientID) {
        this.ingredientID = ingredientID;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
