package com.chefathands.recommendation.service;

import com.chefathands.recommendation.dto.IngredientRequest;
import com.chefathands.recommendation.dto.RecipeFilters;
import com.chefathands.recommendation.model.Recipe;
import com.chefathands.recommendation.model.RecipeIngredient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    /**
     * Get recipe recommendations based on user's saved ingredients
     * 
     * @param userId User ID
     * @param offset Pagination offset
     * @param limit Pagination limit
     * @param filters Recipe filters (nutritional values, category)
     * @return List of recommended recipes
     */
    public List<Recipe> getRecommendationsForUser(Long userId, Integer offset, Integer limit, RecipeFilters filters) {
        // TODO: Implement actual logic to:
        // 1. Fetch user's saved ingredients from ingredient-service or database
        // 2. Query recipe database/service for matching recipes
        // 3. Apply filters (nutritional values, category)
        // 4. Apply pagination
        
        // Mock data for now
        List<Recipe> allRecipes = getMockRecipes();
        
        // Apply filters
        List<Recipe> filteredRecipes = applyFilters(allRecipes, filters);
        
        // Apply pagination
        return applyPagination(filteredRecipes, offset, limit);
    }

    /**
     * Get recipe recommendations based on provided ingredients
     * 
     * @param ingredients List of ingredients with quantities
     * @param offset Pagination offset
     * @param limit Pagination limit
     * @param filters Recipe filters (nutritional values, category)
     * @return List of recommended recipes
     */
    public List<Recipe> getRecommendationsByIngredients(List<IngredientRequest> ingredients, 
                                                         Integer offset, 
                                                         Integer limit, 
                                                         RecipeFilters filters) {
        // TODO: Implement actual logic to:
        // 1. Match recipes that can be made with provided ingredients
        // 2. Rank by ingredient match percentage
        // 3. Apply filters
        // 4. Apply pagination
        
        // Mock data for now
        List<Recipe> allRecipes = getMockRecipes();
        
        // Filter recipes that contain at least one of the provided ingredients
        List<Recipe> matchingRecipes = filterByIngredients(allRecipes, ingredients);
        
        // Apply additional filters
        List<Recipe> filteredRecipes = applyFilters(matchingRecipes, filters);
        
        // Apply pagination
        return applyPagination(filteredRecipes, offset, limit);
    }

    /**
     * Get total count of recipes matching criteria for user
     */
    public int getTotalCountForUser(Long userId, RecipeFilters filters) {
        // TODO: Implement actual count query
        List<Recipe> allRecipes = getMockRecipes();
        return applyFilters(allRecipes, filters).size();
    }

    /**
     * Get total count of recipes matching criteria for ingredients
     */
    public int getTotalCountByIngredients(List<IngredientRequest> ingredients, RecipeFilters filters) {
        // TODO: Implement actual count query
        List<Recipe> allRecipes = getMockRecipes();
        List<Recipe> matchingRecipes = filterByIngredients(allRecipes, ingredients);
        return applyFilters(matchingRecipes, filters).size();
    }

    // Helper methods

    private List<Recipe> applyFilters(List<Recipe> recipes, RecipeFilters filters) {
        if (filters == null) {
            return recipes;
        }

        return recipes.stream()
            .filter(recipe -> {
                if (filters.getCategory() != null && !filters.getCategory().equalsIgnoreCase(recipe.getCategory())) {
                    return false;
                }
                if (filters.getMinProtein() != null && (recipe.getProtein() == null || recipe.getProtein() < filters.getMinProtein())) {
                    return false;
                }
                if (filters.getMaxProtein() != null && (recipe.getProtein() == null || recipe.getProtein() > filters.getMaxProtein())) {
                    return false;
                }
                if (filters.getMinCarbs() != null && (recipe.getCarbs() == null || recipe.getCarbs() < filters.getMinCarbs())) {
                    return false;
                }
                if (filters.getMaxCarbs() != null && (recipe.getCarbs() == null || recipe.getCarbs() > filters.getMaxCarbs())) {
                    return false;
                }
                if (filters.getMinFat() != null && (recipe.getFat() == null || recipe.getFat() < filters.getMinFat())) {
                    return false;
                }
                if (filters.getMaxFat() != null && (recipe.getFat() == null || recipe.getFat() > filters.getMaxFat())) {
                    return false;
                }
                if (filters.getMinCalories() != null && (recipe.getCalories() == null || recipe.getCalories() < filters.getMinCalories())) {
                    return false;
                }
                if (filters.getMaxCalories() != null && (recipe.getCalories() == null || recipe.getCalories() > filters.getMaxCalories())) {
                    return false;
                }
                return true;
            })
            .collect(Collectors.toList());
    }

    private List<Recipe> filterByIngredients(List<Recipe> recipes, List<IngredientRequest> ingredients) {
        if (ingredients == null || ingredients.isEmpty()) {
            return recipes;
        }

        List<Long> ingredientIds = ingredients.stream()
            .map(IngredientRequest::getIngredientID)
            .collect(Collectors.toList());

        return recipes.stream()
            .filter(recipe -> {
                if (recipe.getIngredients() == null || recipe.getIngredients().isEmpty()) {
                    return false;
                }
                return recipe.getIngredients().stream()
                    .anyMatch(ri -> ingredientIds.contains(ri.getIngredientId()));
            })
            .collect(Collectors.toList());
    }

    private List<Recipe> applyPagination(List<Recipe> recipes, Integer offset, Integer limit) {
        int start = (offset != null) ? offset : 0;
        int end = (limit != null) ? Math.min(start + limit, recipes.size()) : recipes.size();
        
        if (start >= recipes.size()) {
            return new ArrayList<>();
        }
        
        return recipes.subList(start, end);
    }

    // Mock data - replace with actual database/service calls
    private List<Recipe> getMockRecipes() {
        List<Recipe> recipes = new ArrayList<>();

        Recipe recipe1 = new Recipe(1L, "Tomato Pasta", "Delicious pasta with tomatoes", "dinner");
        recipe1.setCalories(450);
        recipe1.setProtein(15);
        recipe1.setCarbs(60);
        recipe1.setFat(12);
        recipe1.setPreparationTime(30);
        recipe1.setServings(2);
        recipe1.setIngredients(Arrays.asList(
            new RecipeIngredient(1L, "tomato", 4, "pcs"),
            new RecipeIngredient(2L, "pasta", 200, "g")
        ));
        recipe1.setInstructions(Arrays.asList(
            "Boil water and cook pasta",
            "Prepare tomato sauce",
            "Mix and serve"
        ));
        recipes.add(recipe1);

        Recipe recipe2 = new Recipe(2L, "Chocolate Cake", "Rich chocolate dessert", "dessert");
        recipe2.setCalories(550);
        recipe2.setProtein(8);
        recipe2.setCarbs(75);
        recipe2.setFat(25);
        recipe2.setPreparationTime(60);
        recipe2.setServings(8);
        recipe2.setIngredients(Arrays.asList(
            new RecipeIngredient(5L, "milk", 200, "ml"),
            new RecipeIngredient(6L, "chocolate", 100, "g"),
            new RecipeIngredient(7L, "flour", 250, "g")
        ));
        recipe2.setInstructions(Arrays.asList(
            "Mix dry ingredients",
            "Add wet ingredients",
            "Bake at 180C for 45 minutes"
        ));
        recipes.add(recipe2);

        Recipe recipe3 = new Recipe(3L, "Meat Stew", "Hearty meat stew", "dinner");
        recipe3.setCalories(600);
        recipe3.setProtein(45);
        recipe3.setCarbs(30);
        recipe3.setFat(35);
        recipe3.setPreparationTime(120);
        recipe3.setServings(4);
        recipe3.setIngredients(Arrays.asList(
            new RecipeIngredient(3L, "meat", 500, "g"),
            new RecipeIngredient(1L, "tomato", 2, "pcs"),
            new RecipeIngredient(8L, "onion", 1, "pcs")
        ));
        recipe3.setInstructions(Arrays.asList(
            "Brown the meat",
            "Add vegetables and liquid",
            "Simmer for 2 hours"
        ));
        recipes.add(recipe3);

        Recipe recipe4 = new Recipe(4L, "Vegan Breakfast Bowl", "Healthy morning bowl", "breakfast");
        recipe4.setCalories(350);
        recipe4.setProtein(12);
        recipe4.setCarbs(55);
        recipe4.setFat(8);
        recipe4.setPreparationTime(15);
        recipe4.setServings(1);
        recipe4.setIngredients(Arrays.asList(
            new RecipeIngredient(9L, "oats", 50, "g"),
            new RecipeIngredient(10L, "banana", 1, "pcs"),
            new RecipeIngredient(5L, "milk", 150, "ml")
        ));
        recipe4.setInstructions(Arrays.asList(
            "Cook oats with milk",
            "Top with sliced banana",
            "Serve warm"
        ));
        recipes.add(recipe4);

        return recipes;
    }
}
