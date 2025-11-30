package com.chefathands.recommendation.dto;

public class RecipeFilters {
    private Integer minCarbs;
    private Integer maxCarbs;
    private Integer minProtein;
    private Integer maxProtein;
    private Integer minFat;
    private Integer maxFat;
    private Integer minCalories;
    private Integer maxCalories;
    private String category; // breakfast, vegan, dessert, etc.

    public RecipeFilters() {}

    public Integer getMinCarbs() {
        return minCarbs;
    }

    public void setMinCarbs(Integer minCarbs) {
        this.minCarbs = minCarbs;
    }

    public Integer getMaxCarbs() {
        return maxCarbs;
    }

    public void setMaxCarbs(Integer maxCarbs) {
        this.maxCarbs = maxCarbs;
    }

    public Integer getMinProtein() {
        return minProtein;
    }

    public void setMinProtein(Integer minProtein) {
        this.minProtein = minProtein;
    }

    public Integer getMaxProtein() {
        return maxProtein;
    }

    public void setMaxProtein(Integer maxProtein) {
        this.maxProtein = maxProtein;
    }

    public Integer getMinFat() {
        return minFat;
    }

    public void setMinFat(Integer minFat) {
        this.minFat = minFat;
    }

    public Integer getMaxFat() {
        return maxFat;
    }

    public void setMaxFat(Integer maxFat) {
        this.maxFat = maxFat;
    }

    public Integer getMinCalories() {
        return minCalories;
    }

    public void setMinCalories(Integer minCalories) {
        this.minCalories = minCalories;
    }

    public Integer getMaxCalories() {
        return maxCalories;
    }

    public void setMaxCalories(Integer maxCalories) {
        this.maxCalories = maxCalories;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
