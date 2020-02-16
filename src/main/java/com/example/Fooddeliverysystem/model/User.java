package com.example.Fooddeliverysystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * The persistent class for the user database table.
 */
@ApiModel
@Entity
@NamedQuery(name = "User.findAll", query = "SELECT u FROM User u")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    @NotEmpty(message = "Email address is empty!")
    private String email;

    @NotEmpty(message = "Last name is empty!")
    @Column(name = "last_name")
    private String lastName;

    @NotEmpty(message = "Name is empty!")
    private String name;

    @NotEmpty(message = "Password can not be empty")
    @Size(min = 6, message = "Must be longer ;)")
    private String password;


    private String pathImage;

    @JsonIgnore
    private String token;

    @JsonIgnore
    private int status;

    @JsonIgnore
    @Column(name = "theChosenOne", insertable = false)
    private boolean chosenOne;


    //bi-directional many-to-one association to Confirmationtoken
    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Confirmationtoken> confirmationtokens;

    //bi-directional many-to-one association to Role
    @JsonIgnore
    @ManyToOne
    private Role role;

    //bi-directional many-to-one association to Userorder
    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Userorder> userorders;

    public User() {
        userorders = new ArrayList<>();
    }

    public int getUserId() {
        return this.userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPathImage() {
        return this.pathImage;
    }

    public void setPathImage(String pathImage) {
        this.pathImage = pathImage;
    }

    public boolean getChosenOne() {
        return this.chosenOne;
    }

    public void setChosenOne(boolean chosenOne) {
        this.chosenOne = chosenOne;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<Confirmationtoken> getConfirmationtokens() {
        return this.confirmationtokens;
    }

    public void setConfirmationtokens(List<Confirmationtoken> confirmationtokens) {
        this.confirmationtokens = confirmationtokens;
    }

    public Confirmationtoken addConfirmationtoken(Confirmationtoken confirmationtoken) {
        getConfirmationtokens().add(confirmationtoken);
        confirmationtoken.setUser(this);

        return confirmationtoken;
    }

    public Confirmationtoken removeConfirmationtoken(Confirmationtoken confirmationtoken) {
        getConfirmationtokens().remove(confirmationtoken);
        confirmationtoken.setUser(null);

        return confirmationtoken;
    }

    public Role getRole() {
        return this.role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<Userorder> getUserorders() {
        return this.userorders;
    }

    public void setUserorders(List<Userorder> userorders) {
        this.userorders = userorders;
    }

    public Userorder addUserorder(Userorder userorder) {
        getUserorders().add(userorder);
        userorder.setUser(this);

        return userorder;
    }

    public boolean removeUserorder(Userorder userorder) {
        for (Userorder userorder1 : userorders) {
            if (userorder1.getUserOrderId() == userorder.getUserOrderId()) {
                userorders.remove(userorder1);
                return true;
            }
        }
        return false;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}