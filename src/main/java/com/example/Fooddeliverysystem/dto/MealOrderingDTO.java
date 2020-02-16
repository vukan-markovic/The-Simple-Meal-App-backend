package com.example.Fooddeliverysystem.dto;

import com.example.Fooddeliverysystem.model.Meal;
import com.sun.istack.NotNull;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import java.io.Serializable;

public class MealOrderingDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    @NotNull
    private Meal meal;

    @NotEmpty(message = "Regular is empty.")
    private String regular; //possible values are small, big or false

    @Positive(message = "Count isn't a positive number.")
    private int count;

    public Meal getMeal() {
        return meal;
    }

    public void setMeal(Meal meal) {
        this.meal = meal;
    }

    public String getRegular() {
        return regular;
    }

    public void setRegular(String regular) {
        this.regular = regular;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}