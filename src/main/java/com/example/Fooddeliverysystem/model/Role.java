package com.example.Fooddeliverysystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;

import java.io.Serializable;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The persistent class for the role database table.
 */
@ApiModel
@Entity
@NamedQuery(name = "Role.findAll", query = "SELECT r FROM Role r")
public class Role implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int roleId;

    private String name;

    //bi-directional many-to-one association to User
    @JsonIgnore
    @OneToMany(mappedBy = "role")
    private List<User> users;

    public Role() {
        users = new ArrayList<>();
    }

    public int getRoleId() {
        return this.roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<User> getUsers() {
        return this.users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public User addUser(User user) {
        getUsers().add(user);
        user.setRole(this);

        return user;
    }

    public User removeUser(User user) {
        getUsers().remove(user);
        user.setRole(null);

        return user;
    }
}