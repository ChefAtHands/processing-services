package com.chefathands.recommendation.model;

import java.util.List;

public class AnalyzedInstruction {
    private String name;
    private List<InstructionStep> steps;

    public AnalyzedInstruction() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<InstructionStep> getSteps() { return steps; }
    public void setSteps(List<InstructionStep> steps) { this.steps = steps; }
}