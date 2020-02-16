package com.example.Fooddeliverysystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;

/**
 * The persistent class for the mealtype database table.
 */
@ApiModel
@Entity
@NamedQuery(name = "Mealtype.findAll", query = "SELECT m FROM MealType m")
public class MealType implements Serializable {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private MealtypePK id;

    //bi-directional many-to-many association to Userorder
    @ManyToMany(mappedBy = "mealtypes")
    @JsonIgnore
    private List<Userorder> userorders;

    public MealType() {
    }

    public MealtypePK getId() {
        return this.id;
    }

    public void setId(MealtypePK id) {
        this.id = id;
    }

    public List<Userorder> getUserorders() {
        return this.userorders;
    }

    public void setUserorders(List<Userorder> userorders) {
        this.userorders = userorders;
    }

    public void addUserOrder(Userorder userorderForAdd) {
        for (Userorder userorder : userorders) {
            if (userorderForAdd.getUserOrderId() == userorder.getUserOrderId()) {
                return;
            }
        }
        userorders.add(userorderForAdd);
    }

    public boolean removeUserOrder(Userorder userorder) {
        for (Userorder userorder1 : userorders) {
            if (userorder1.getUserOrderId() == userorder.getUserOrderId()) {
                userorders.remove(userorder1);
                return true;
            }
        }
        return false;
    }
}