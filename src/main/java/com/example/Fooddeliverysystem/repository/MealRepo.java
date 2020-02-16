package com.example.Fooddeliverysystem.repository;

import com.example.Fooddeliverysystem.model.Meal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.lang.Integer;

import org.springframework.stereotype.Repository;

@Repository
public interface MealRepo extends JpaRepository<Meal, Integer> {
    Meal getByMealId(int id);
}