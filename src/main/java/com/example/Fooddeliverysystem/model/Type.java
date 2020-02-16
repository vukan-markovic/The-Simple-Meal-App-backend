package com.example.Fooddeliverysystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The persistent class for the type database table.
 */
@ApiModel
@Entity
@NamedQuery(name = "Type.findAll", query = "SELECT t FROM Type t")
public class Type implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int typeId;

    @NotNull
    @NotEmpty
    private String name;

    @NotNull
    @Positive
    private double price;

    @NotNull
    private boolean regular;

    //bi-directional many-to-many association to Meal
    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "mealtype"
            , joinColumns = {
            @JoinColumn(name = "Type_typeId")
    }
            , inverseJoinColumns = {
            @JoinColumn(name = "Meal_mealId")
    }
    )
    private List<Meal> meals;

    public Type() {
        meals = new ArrayList<>();
    }

    public int getTypeId() {
        return this.typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<Meal> getMeals() {
        return this.meals;
    }

    public void setMeals(List<Meal> meals) {
        this.meals = meals;
    }

    public void addMeal(Meal meal) {
        meals.add(meal);
    }

    public boolean isRegular() {
        return regular;
    }

    public void setRegular(boolean regular) {
        this.regular = regular;
    }

    @Override
    public String toString() {
        return " " + name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Type type = (Type) o;
        return typeId == type.typeId;
    }

    @Override
    public int hashCode() {
        return (int) (typeId + Objects.hashCode(name) + price);
    }

}