package com.example.Fooddeliverysystem.model;

import io.swagger.annotations.ApiModel;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the mealtype database table.
 */
@ApiModel
@Embeddable
public class MealtypePK implements Serializable {
    //default serial version id, required for serializable classes.
    private static final long serialVersionUID = 1L;

    @Column(insertable = false, updatable = false)
    private int meal_mealId;

    @Column(insertable = false, updatable = false)
    private int type_typeId;

    public MealtypePK() {
    }

    public int getMeal_mealId() {
        return this.meal_mealId;
    }

    public void setMeal_mealId(int meal_mealId) {
        this.meal_mealId = meal_mealId;
    }

    public int getType_typeId() {
        return this.type_typeId;
    }

    public void setType_typeId(int type_typeId) {
        this.type_typeId = type_typeId;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof MealtypePK)) {
            return false;
        }
        MealtypePK castOther = (MealtypePK) other;
        return
                (this.meal_mealId == castOther.meal_mealId)
                        && (this.type_typeId == castOther.type_typeId);
    }

    public int hashCode() {
        final int prime = 31;
        int hash = 17;
        hash = hash * prime + this.meal_mealId;
        hash = hash * prime + this.type_typeId;

        return hash;
    }
}