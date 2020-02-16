package com.example.Fooddeliverysystem.dto;

import com.example.Fooddeliverysystem.model.Meal;

import javax.validation.constraints.NotNull;
import java.util.List;

public class DailyMenuForUpdateDTO {
    @NotNull
    private int dailyMenuId;

    @NotNull
    private List<Meal> meals;

    public int getDailyMenuId() {
        return dailyMenuId;
    }

    public void setDailyMenuId(int dailyMenuId) {
        this.dailyMenuId = dailyMenuId;
    }

    public List<Meal> getMeals() {
        return meals;
    }

    public void setMeals(List<Meal> meals) {
        this.meals = meals;
    }
}