package com.example.Fooddeliverysystem.dto;

import javax.validation.constraints.NotEmpty;

public class LoginDTO {

    @NotEmpty(message = "Email address is empty!")
    private String email;

    @NotEmpty(message = "Password is empty!")
    private String password;
    private String jwtToken;

    public LoginDTO() {
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }
}