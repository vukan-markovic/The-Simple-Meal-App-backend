package com.example.Fooddeliverysystem.dto;

import com.example.Fooddeliverysystem.model.Meal;
import com.example.Fooddeliverysystem.model.Type;
import com.sun.istack.NotNull;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import java.io.Serializable;

public class MealDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotEmpty(message = "Name is empty.")
    private String name;
    @NotEmpty(message = "Description is empty")
    private String description;

    @NotNull()
    private boolean earlyorder;

    private Type type;

    private Double price;

    public Meal toMeal() {
        Meal meal = new Meal();
        meal.setName(name);
        meal.setDescription(description);
        meal.setEarlyOrder(earlyorder);
        System.out.println(meal.getName() + " " + meal.getDescription() + " " + meal.getEarlyOrder());
        return meal;
    }

    public Type getType() {
        return type;
    }

    public Double getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isEarlyOrder() {
        return earlyorder;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setEarlyOrder(boolean earlyorder) {
        this.earlyorder = earlyorder;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}