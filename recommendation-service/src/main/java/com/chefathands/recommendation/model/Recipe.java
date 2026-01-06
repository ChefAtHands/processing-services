package com.chefathands.recommendation.model;

import java.util.List;

public class Recipe {

    private Long id;
    private String title;
    private String image;
    private Integer readyInMinutes;
    private Integer servings;
    private String sourceUrl;
    private String summary;

    private List<ExtendedIngredient> extendedIngredients;
    private List<AnalyzedInstruction> analyzedInstructions;

    public Recipe() {}

    // Getters and setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public Integer getReadyInMinutes() { return readyInMinutes; }
    public void setReadyInMinutes(Integer readyInMinutes) { this.readyInMinutes = readyInMinutes; }

    public Integer getServings() { return servings; }
    public void setServings(Integer servings) { this.servings = servings; }

    public String getSourceUrl() { return sourceUrl; }
    public void setSourceUrl(String sourceUrl) { this.sourceUrl = sourceUrl; }

    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }

    public List<ExtendedIngredient> getExtendedIngredients() { return extendedIngredients; }
    public void setExtendedIngredients(List<ExtendedIngredient> extendedIngredients) {
        this.extendedIngredients = extendedIngredients;
    }

    public List<AnalyzedInstruction> getAnalyzedInstructions() { return analyzedInstructions; }
    public void setAnalyzedInstructions(List<AnalyzedInstruction> analyzedInstructions) {
        this.analyzedInstructions = analyzedInstructions;
    }
}