package com.example.Fooddeliverysystem.service;

import com.example.Fooddeliverysystem.dto.AddMealDTO;
import com.example.Fooddeliverysystem.dto.MealEditDTO;
import com.example.Fooddeliverysystem.model.Meal;

import java.util.List;

public interface MealIServiceI {

    //public Meal addMeal(MealDTO mealDto);
    Meal editMeal(MealEditDTO mealEditDTO);

    Meal getMeal(Integer id);

    boolean deleteMeal(int id);

    List<Meal> getMeals();

    Meal insertMeal(AddMealDTO addMealDTO);
}