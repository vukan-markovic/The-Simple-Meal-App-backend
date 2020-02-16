package com.example.Fooddeliverysystem.dto;

import com.example.Fooddeliverysystem.model.User;

public class UserDTO {

    private int userId;
    private String email;
    private String lastName;
    private String name;
    private String pathImage;
    private int status;
    private String role;

    public static UserDTO mapper(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(user.getUserId());
        userDTO.setEmail(user.getEmail());
        userDTO.setLastName(user.getLastName());
        userDTO.setName(user.getName());
        userDTO.setPathImage(user.getPathImage());
        userDTO.setStatus(user.getStatus());
        userDTO.setRole(user.getRole().getName());

        return userDTO;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPathImage() {
        return pathImage;
    }

    public void setPathImage(String pathImage) {
        this.pathImage = pathImage;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}