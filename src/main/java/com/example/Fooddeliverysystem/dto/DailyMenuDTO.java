package com.example.Fooddeliverysystem.dto;

import com.example.Fooddeliverysystem.model.Meal;
import com.example.Fooddeliverysystem.model.Weaklymenu;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

public class DailyMenuDTO {

    private Date date;

    private List<Meal> meals;

    private Weaklymenu weeklyMenu;

    public DailyMenuDTO() {
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<Meal> getMeals() {
        return meals;
    }

    public void setMeals(List<Meal> meals) {
        this.meals = meals;
    }

    public Weaklymenu getWeeklyMenu() {
        return weeklyMenu;
    }

    public void setWeeklyMenu(Weaklymenu weaklyMenu) {
        this.weeklyMenu = weaklyMenu;
    }
}