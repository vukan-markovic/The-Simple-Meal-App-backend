package com.example.Fooddeliverysystem.model;

import io.swagger.annotations.ApiModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;


import java.io.Serializable;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The persistent class for the dailymenu database table.
 */
@ApiModel
@Entity
@Table(name = "DailyMenu")
@NamedQuery(name = "Dailymenu.findAll", query = "SELECT d FROM Dailymenu d")
public class Dailymenu implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int dailyMenuId;

    @Temporal(TemporalType.DATE)
    private Date date;

    //bi-directional many-to-one association to Weaklymenu
    @ManyToOne
    @JsonIgnore
    private Weaklymenu weaklymenu;

    //bi-directional many-to-many association to Meal
    @ManyToMany(mappedBy = "dailymenus")
    private List<Meal> meals;

    public Dailymenu() {
        meals = new ArrayList<>();
    }

    public int getDailyMenuId() {
        return this.dailyMenuId;
    }

    public void setDailyMenuId(int dailyMenuId) {
        this.dailyMenuId = dailyMenuId;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Weaklymenu getWeaklymenu() {
        return this.weaklymenu;
    }

    public void setWeaklymenu(Weaklymenu weaklymenu) {
        this.weaklymenu = weaklymenu;
    }

    public List<Meal> getMeals() {
        return this.meals;
    }

    public void setMeals(List<Meal> meals) {
        this.meals = meals;
    }

    public boolean existMeal(int id) {
        for (Meal meal : meals
        ) {
            if (meal.getMealId() == id) {
                return true;
            }
        }
        return false;
    }
    public void removeMeal(Meal meal){
        if(meals == null && meals.isEmpty()){
            return;
        }
        for (Meal current: meals) {
            if(current.getMealId() == meal.getMealId()){
                meals.remove(current);
                return;
            }
        }

    }
}