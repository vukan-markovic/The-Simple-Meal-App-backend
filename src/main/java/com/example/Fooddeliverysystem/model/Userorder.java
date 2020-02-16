package com.example.Fooddeliverysystem.model;

import io.swagger.annotations.ApiModel;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the userorder database table.
 */
@ApiModel
@Entity
@NamedQuery(name = "Userorder.findAll", query = "SELECT u FROM Userorder u")
public class Userorder implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userOrderId;

    @Temporal(TemporalType.DATE)
    @NotNull
    private Date date;

    private boolean paid;

    //bi-directional many-to-many association to Mealtype

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "mealtypeuserorder"
            , joinColumns = {
            @JoinColumn(name = "UserOrder_userOrderId")
    }
            , inverseJoinColumns = {
            @JoinColumn(name = "MealType_Meal_mealId", referencedColumnName = "Meal_mealId"),
            @JoinColumn(name = "MealType_Type_typeId", referencedColumnName = "Type_typeId")
    }
    )
    private List<MealType> mealtypes;

    //bi-directional many-to-one association to User
    @ManyToOne
    private User user;

    public Userorder() {
        mealtypes = new ArrayList<>();
    }

    public int getUserOrderId() {
        return this.userOrderId;
    }

    public void setUserOrderId(int userOrderId) {
        this.userOrderId = userOrderId;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean getPaid() {
        return this.paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public List<MealType> getMealtypes() {
        return this.mealtypes;
    }

    public void setMealtypes(List<MealType> mealtypes) {
        this.mealtypes = mealtypes;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}