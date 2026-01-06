package com.chefathands.recommendation.model;

public class ExtendedIngredient {
    private String original;
    private String name;
    private Double amount;
    private String unit;

    public ExtendedIngredient() {}

    public String getOriginal() { return original; }
    public void setOriginal(String original) { this.original = original; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
}