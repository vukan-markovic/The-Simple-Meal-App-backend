package com.example.Fooddeliverysystem.service;

import com.example.Fooddeliverysystem.dto.MealTypeDTO;
import com.example.Fooddeliverysystem.dto.UserDTO;

public interface ChosenOneServiceI {

    void selectChosenOne();

    void setPaid(MealTypeDTO mealTypeDTO);

    UserDTO getChosenOne();

    void payingNotification(int[] userId);
}
