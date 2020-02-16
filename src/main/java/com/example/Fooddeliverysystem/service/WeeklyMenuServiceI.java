package com.example.Fooddeliverysystem.service;

import com.example.Fooddeliverysystem.dto.WeeklyMenuDTO;
import com.example.Fooddeliverysystem.model.Weaklymenu;

import java.util.Collection;

public interface WeeklyMenuServiceI {

    Weaklymenu getCurrentWeekMenu();

    Weaklymenu saveWeeklyMenu(WeeklyMenuDTO menu);

    Collection<Weaklymenu> getAllWeeklyMenu();

    Collection<Weaklymenu> getWeeklyMenusForDailyMenu();
}
