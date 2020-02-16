package com.example.Fooddeliverysystem.dto;

import com.sun.istack.NotNull;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

public class MealEditDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private int id;

    @NotEmpty(message = "Name is empty")
    private String name;
    @NotEmpty(message = "Description is empty")
    private String description;

    @NotNull
    private boolean earlyorder;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isEarlyorder() {
        return earlyorder;
    }

    public void setId(int id) {
        this.id = id;
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
}