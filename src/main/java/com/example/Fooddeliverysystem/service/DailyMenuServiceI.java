package com.example.Fooddeliverysystem.service;

import com.example.Fooddeliverysystem.dto.DailyMenuDTO;
import com.example.Fooddeliverysystem.dto.DailyMenuForUpdateDTO;
import com.example.Fooddeliverysystem.model.Dailymenu;

import java.util.Collection;
import java.util.Date;

public interface DailyMenuServiceI {
    Dailymenu getDailyMenuForToday();

    Collection<Dailymenu> getDailyMenus(Integer weaklyMenuId);

    Dailymenu saveDailyMenu(DailyMenuDTO menu);

    Dailymenu updateDailyMenu(DailyMenuForUpdateDTO dailyMenuForUpdateDTO);

    Collection<Date> getDays(int weeklyMenuId);
}