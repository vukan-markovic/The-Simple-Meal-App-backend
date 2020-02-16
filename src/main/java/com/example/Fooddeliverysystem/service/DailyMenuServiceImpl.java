package com.example.Fooddeliverysystem.service;

import com.example.Fooddeliverysystem.dto.DailyMenuDTO;
import com.example.Fooddeliverysystem.dto.DailyMenuForUpdateDTO;
import com.example.Fooddeliverysystem.model.Dailymenu;
import com.example.Fooddeliverysystem.model.Meal;
import com.example.Fooddeliverysystem.model.Weaklymenu;
import com.example.Fooddeliverysystem.repository.DailyMenuRepo;
import com.example.Fooddeliverysystem.repository.MealRepo;
import com.example.Fooddeliverysystem.repository.WeeklyMenuRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DailyMenuServiceImpl implements DailyMenuServiceI {

    private DailyMenuRepo dailyMenuRepo;

    private WeeklyMenuRepo weeklyMenuRepo;

    private MealRepo mealRepo;
    private Environment environment;

    @Autowired
    public void setEnvironment(Environment environment) { this.environment = environment; }

    private Date getDate(Date date, int hour, int min, int sec){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.SECOND, sec);
        Date newDate=  calendar.getTime();
        return newDate;
    }

    @Override
    public Dailymenu getDailyMenuForToday() throws NumberFormatException{
        Dailymenu menuToday = dailyMenuRepo.findByDate(new Date());

        Dailymenu menuTomorrow = dailyMenuRepo.findByDate(getTomorrow(new Date()));
        List<Meal> meals = new ArrayList<>();

        Date today = new Date();
        Date todayTenClock = getDate(new Date(),orderingOrderUntilHour(),orderingOrderUntilMin(),0);

        Date todayFiveClock = getDate(new Date(),orderingEarlyOrderUntilHour(),orderingEarlyOrderUntilMin(),0);

        if (menuToday != null) {
            for (int i = 0; i < menuToday.getMeals().size(); i++) {
                if (!menuToday.getMeals().get(i).getEarlyOrder() && !today.after(todayTenClock))
                    meals.add(menuToday.getMeals().get(i));
            }
        }

        if (menuTomorrow != null) {
            for (int i = 0; i < menuTomorrow.getMeals().size(); i++) {
                if (menuTomorrow.getMeals().get(i).getEarlyOrder() && !today.after(todayFiveClock))
                    meals.add(menuTomorrow.getMeals().get(i));
            }
        }

        if (menuToday != null) {
            menuToday.setMeals(meals);
            return menuToday;
        }

        return null;
    }

    @Override
    public Collection<Dailymenu> getDailyMenus(Integer weaklyMenuId) {
        Weaklymenu weaklymenu = weeklyMenuRepo.findByWeaklyMenuId(weaklyMenuId);
        if (weaklymenu == null) {
            return new ArrayList<>();
        }
        return weaklymenu.getDailymenus();
    }

    @Override
    public Dailymenu saveDailyMenu(DailyMenuDTO menuDTO) throws NumberFormatException {
        if (dailyMenuRepo.findByDate(menuDTO.getDate()) != null) {
            return null;
        }
        Weaklymenu weaklymenu = weeklyMenuRepo.findByWeaklyMenuId(menuDTO.getWeeklyMenu().getWeaklyMenuId());
        if (weaklymenu == null) {
            return null;
        }
        if (weaklymenu.haveDailyMenu(menuDTO.getDate())) {
            return null;
        }
        if (menuDTO.getDate().before(weaklymenu.getDateFrom()) || menuDTO.getDate().after(getTomorrow(weaklymenu.getDateTo()))) {
            return null;
        }
        Dailymenu menu = new Dailymenu();
        for (Meal meal : menuDTO.getMeals()) {
            if (mealRepo.getByMealId(meal.getMealId()) == null) {
                return null;
            }
        }
        menu.setDate(menuDTO.getDate());
        menu.setMeals(menuDTO.getMeals());
        menu.setWeaklymenu(weaklymenu);

        Dailymenu newMenu = dailyMenuRepo.save(menu);

        weaklymenu.addDailymenus(newMenu);
        weeklyMenuRepo.save(weaklymenu);

        for (Meal meal : menuDTO.getMeals()) {
            Meal meal2 = mealRepo.getByMealId(meal.getMealId());
            meal2.addDailyMenu(newMenu);
            mealRepo.save(meal2);
        }
        return newMenu;
    }

    private boolean checkDateForUpdate(Date dateForCheck) throws NumberFormatException{
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.add(Calendar.DATE, 1);
        Date dateTomorrow = c.getTime();

        if(dateForCheck.before(dateTomorrow)){
            return false;
        }
        return true;
    }

    @Override
    public Dailymenu updateDailyMenu(DailyMenuForUpdateDTO dailyMenuForUpdateDTO) throws NumberFormatException {
        Dailymenu dailymenu = dailyMenuRepo.findByDailyMenuId(dailyMenuForUpdateDTO.getDailyMenuId());
        if (dailymenu == null) {
            return null;
        }
        if(!checkDateForUpdate(dailymenu.getDate())){
            return null;
        }
        for (Meal meal : dailyMenuForUpdateDTO.getMeals()) {
            if (mealRepo.getByMealId(meal.getMealId()) == null) {
                return null;
            }
        }

        for (Meal meal : dailymenu.getMeals()) {
            Meal meal2 = mealRepo.getByMealId(meal.getMealId());
            meal2.removeDailyMenu(dailymenu);
            mealRepo.save(meal2);
        }

        dailymenu.setMeals(dailyMenuForUpdateDTO.getMeals());
        Dailymenu newMenu = dailyMenuRepo.save(dailymenu);

        for (Meal meal : dailyMenuForUpdateDTO.getMeals()) {
            Meal meal2 = mealRepo.getByMealId(meal.getMealId());
            meal2.addDailyMenu(newMenu);
            mealRepo.save(meal2);
        }
        return newMenu;
    }

    private Date getTomorrow(Date today){
        Calendar c = Calendar.getInstance();
        c.setTime(today);
        c.add(Calendar.DATE, 1);
        return c.getTime();
    }
    @Override
    public Collection<Date> getDays(int weeklyMenuId) {
        ArrayList<Date> dates = new ArrayList<>();
        Weaklymenu weeklymenu = weeklyMenuRepo.findByWeaklyMenuId(weeklyMenuId);
        if (weeklymenu == null) {
            return dates;
        }
        Date day = weeklymenu.getDateFrom();

        while (day.before(getTomorrow(weeklymenu.getDateTo()))) {
            dates.add(day);
            day = getTomorrow(day);
        }
        ArrayList<Date> datesRet = new ArrayList<>();
        for (Date date : dates) {
            if (!weeklymenu.haveDailyMenu(date)) {
                datesRet.add(date);
            }
        }
        return datesRet;
    }

    @Autowired
    public void setMealRepo(MealRepo mealRepo) {
        this.mealRepo = mealRepo;
    }

    @Autowired
    public void setDailyMenuRepo(DailyMenuRepo dailyMenuRepo) {
        this.dailyMenuRepo = dailyMenuRepo;
    }

    @Autowired
    public void setWeeklyMenuRepo(WeeklyMenuRepo weeklyMenuRepo) {
        this.weeklyMenuRepo = weeklyMenuRepo;
    }

    private int orderingOrderUntilHour() {
        return Integer.parseInt(environment.getProperty("ordering.order.until.hour"));
    }

    private int orderingOrderUntilMin() {
        return Integer.parseInt(environment.getProperty("ordering.order.until.min"));
    }


    private int orderingEarlyOrderUntilHour() {
        return Integer.parseInt(environment.getProperty("ordering.early.order.until.hour"));
    }

    private int orderingEarlyOrderUntilMin() {
        return Integer.parseInt(environment.getProperty("ordering.early.order.until.min"));
    }
}