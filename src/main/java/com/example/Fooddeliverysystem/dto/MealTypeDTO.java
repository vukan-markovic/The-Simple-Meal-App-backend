package com.example.Fooddeliverysystem.dto;

import com.example.Fooddeliverysystem.model.Meal;
import com.example.Fooddeliverysystem.model.Type;

import java.io.Serializable;
import java.util.Objects;

public class MealTypeDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Meal meal;
    private Type type;
    private int userOrderId;
    private UserDTO user;
    private boolean paid;

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public Meal getMeal() {
        return meal;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public void setMeal(Meal meal) {
        this.meal = meal;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public int getUserOrderId() {
        return userOrderId;
    }

    public void setUserOrderId(int userOrderId) {
        this.userOrderId = userOrderId;
    }

    @Override
    public String toString() {
        return meal + ", " + type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MealTypeDTO that = (MealTypeDTO) o;
        return userOrderId == that.userOrderId &&
                Objects.equals(meal, that.meal) &&
                Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return (meal.hashCode() + 1) * (type.hashCode() + 1);
    }
}