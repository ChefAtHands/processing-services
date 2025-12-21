package com.chefathands.recommendation.dto;

/**
 * DTO matching the UserIngredient model from ingredient-service
 */
public class UserIngredientDTO {
    private Integer id;
    private Integer userId;
    private Integer ingredientId;
    private String quantity;
    
    // Constructors
    public UserIngredientDTO() {}
    
    public UserIngredientDTO(Integer id, Integer userId, Integer ingredientId, String quantity) {
        this.id = id;
        this.userId = userId;
        this.ingredientId = ingredientId;
        this.quantity = quantity;
    }
    
    // Getters and setters
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public Integer getUserId() {
        return userId;
    }
    
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    
    public Integer getIngredientId() {
        return ingredientId;
    }
    
    public void setIngredientId(Integer ingredientId) {
        this.ingredientId = ingredientId;
    }
    
    public String getQuantity() {
        return quantity;
    }
    
    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
