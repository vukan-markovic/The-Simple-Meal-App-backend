package com.example.Fooddeliverysystem.service;

import com.example.Fooddeliverysystem.dto.WeeklyMenuDTO;
import com.example.Fooddeliverysystem.model.Dailymenu;
import com.example.Fooddeliverysystem.model.Weaklymenu;
import com.example.Fooddeliverysystem.repository.DailyMenuRepo;
import com.example.Fooddeliverysystem.repository.WeeklyMenuRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class WeeklyMenuServiceImpl implements WeeklyMenuServiceI {

    private WeeklyMenuRepo weeklyMenuRepo;

    private DailyMenuRepo dailyMenuRepo;

    @Override
    public Weaklymenu getCurrentWeekMenu() {
        Date today = new Date();
        List<Weaklymenu> weaklyMenus = weeklyMenuRepo.findAll();
        for (Weaklymenu weaklymenu : weaklyMenus) {
            if (weaklymenu.getDateFrom().compareTo(today) * today.compareTo(weaklymenu.getDateTo()) >= 0)
                return weaklymenu;
        }

        return null;
    }

    @Override
    public Weaklymenu saveWeeklyMenu(WeeklyMenuDTO menu) {
        if (menu == null) return null;

        Calendar calendarFrom = Calendar.getInstance();
        calendarFrom.setTime(menu.getFrom());
        calendarFrom.add(Calendar.DATE, 1);

        Calendar calendarTo = Calendar.getInstance();
        calendarTo.setTime(menu.getTo());
        calendarTo.add(Calendar.DATE, 1);

        menu.setFrom(calendarFrom.getTime());
        menu.setTo(calendarTo.getTime());

        Weaklymenu oldMenu = weeklyMenuRepo.findByDateFromAndAndDateTo(menu.getFrom(), menu.getTo());
        List<Weaklymenu> weaklyMenus = weeklyMenuRepo.findAll();

        if (oldMenu != null) return null;

        for (Weaklymenu weaklymenu : weaklyMenus) {
            if (menu.getFrom().compareTo(weaklymenu.getDateFrom()) * weaklymenu.getDateFrom().compareTo(menu.getTo()) >= 0 || menu.getFrom().compareTo(weaklymenu.getDateTo()) * weaklymenu.getDateTo().compareTo(menu.getTo()) >= 0)
                return null;
        }

        Weaklymenu weeklyMenu = new Weaklymenu();
        weeklyMenu.setDateFrom(menu.getFrom());
        weeklyMenu.setDateTo(menu.getTo());
        weeklyMenu.setImagePath(menu.getImage());
        weeklyMenu = weeklyMenuRepo.save(weeklyMenu);
        return createDailyMenus(weeklyMenu);
    }

    private Weaklymenu createDailyMenus(Weaklymenu weeklymenu) {
        Date day = weeklymenu.getDateFrom();
        Calendar c = Calendar.getInstance();
        c.setTime(weeklymenu.getDateTo());
        c.add(Calendar.DATE, 1);

        while (day.before(c.getTime())) {
            Dailymenu menu = new Dailymenu();
            menu.setDate(day);
            menu.setWeaklymenu(weeklymenu);
            Dailymenu newMenu = dailyMenuRepo.save(menu);

            weeklymenu.addDailymenus(newMenu);
            weeklymenu = weeklyMenuRepo.save(weeklymenu);

            Calendar c1 = Calendar.getInstance();
            c1.setTime(day);
            c1.add(Calendar.DATE, 1);
            day = c1.getTime();
        }
        return weeklymenu;
    }

    @Override
    public Collection<Weaklymenu> getAllWeeklyMenu() {
        return weeklyMenuRepo.findAll();
    }

    @Override
    public Collection<Weaklymenu> getWeeklyMenusForDailyMenu() {
        List<Weaklymenu> weaklymenus = weeklyMenuRepo.findAll();
        Date todayDate = new Date();
        List<Weaklymenu> weeklyMenuRet = new ArrayList<>();
        for (Weaklymenu weaklymenu : weaklymenus) {
            if (!weaklymenu.getDateTo().before(todayDate) && !weaklymenu.finishCreation()) {
                weeklyMenuRet.add(weaklymenu);
            }
        }
        return weeklyMenuRet;
    }

    @Autowired
    public void setWeeklyMenuRepo(WeeklyMenuRepo weeklyMenuRepo) {
        this.weeklyMenuRepo = weeklyMenuRepo;
    }

    @Autowired
    public void setDailyMenuRepo(DailyMenuRepo dailyMenuRepo) {
        this.dailyMenuRepo = dailyMenuRepo;
    }
}
