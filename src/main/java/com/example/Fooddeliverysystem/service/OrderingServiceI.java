package com.example.Fooddeliverysystem.service;

import com.example.Fooddeliverysystem.dto.MealOrderingDTO;
import com.example.Fooddeliverysystem.dto.MealTypeDTO;
import com.example.Fooddeliverysystem.model.User;
import com.example.Fooddeliverysystem.model.Userorder;

import java.util.List;

public interface OrderingServiceI {

    User getUser(String token);

    List<Userorder>  ordering(List<MealOrderingDTO> meals, User user);

    List<MealTypeDTO> getOrdering(String forDay, User user);

    boolean deleteOrder(int userorderId, User user);

    List<MealTypeDTO> getAllOrders(String forDay);
}