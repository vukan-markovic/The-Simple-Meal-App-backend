package com.example.Fooddeliverysystem.dto;

import com.example.Fooddeliverysystem.model.Meal;
import com.example.Fooddeliverysystem.model.Type;
import com.sun.istack.NotNull;

import javax.validation.constraints.NotEmpty;
import java.util.List;

public class AddMealDTO {

    @NotNull()
    private Meal meal;

    @NotNull()
    @NotEmpty()
    private List<Type> types;


    public Meal getMeal() {
        return meal;
    }

    public void setMeal(Meal meal) {
        this.meal = meal;
    }

    public List<Type> getTypes() {
        return types;
    }

    public void setTypes(List<Type> typeList) {
        this.types = typeList;
    }
}
