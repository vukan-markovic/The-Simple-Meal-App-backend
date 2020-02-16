package com.example.Fooddeliverysystem.service;

import com.example.Fooddeliverysystem.model.User;

import java.io.IOException;

public interface NotificationServiceI {

    boolean saveToken(String token, User user) throws IOException;

    User getUser(String token);

}