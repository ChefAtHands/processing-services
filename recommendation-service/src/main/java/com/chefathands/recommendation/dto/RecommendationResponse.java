package com.chefathands.recommendation.dto;

import com.chefathands.recommendation.model.Recipe;
import java.util.List;

public class RecommendationResponse {
    private List<Recipe> recipes;
    private Integer total;
    private Integer offset;
    private Integer limit;

    public RecommendationResponse() {}

    public RecommendationResponse(List<Recipe> recipes, Integer total, Integer offset, Integer limit) {
        this.recipes = recipes;
        this.total = total;
        this.offset = offset;
        this.limit = limit;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }
}
