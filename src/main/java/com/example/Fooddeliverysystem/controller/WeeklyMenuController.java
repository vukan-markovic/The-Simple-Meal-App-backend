package com.example.Fooddeliverysystem.controller;

import com.example.Fooddeliverysystem.dto.WeeklyMenuDTO;
import com.example.Fooddeliverysystem.model.Weaklymenu;
import com.example.Fooddeliverysystem.service.WeeklyMenuServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("weekly-menu")
public class WeeklyMenuController {

    private WeeklyMenuServiceI weeklyMenuService;

    @CrossOrigin
    @PostMapping()
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Weaklymenu> saveWeeklyMenu(@RequestBody WeeklyMenuDTO weeklyMenu) {
        Weaklymenu menu = weeklyMenuService.saveWeeklyMenu(weeklyMenu);
        if (menu != null) {
            return new ResponseEntity<>(menu, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/all")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Collection<Weaklymenu>> getWeeklyMenus() {
        Collection<Weaklymenu> result = weeklyMenuService.getAllWeeklyMenu();
        if (result.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/allForDailyMenu")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Collection<Weaklymenu>> getWeeklyMenusForDailyMenu() {
        Collection<Weaklymenu> result = weeklyMenuService.getWeeklyMenusForDailyMenu();
        if (result.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping()
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Weaklymenu> getWeeklyMenu() {
        Weaklymenu menu = weeklyMenuService.getCurrentWeekMenu();

        if (menu != null) {
            return new ResponseEntity<>(menu, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
    }

    @Autowired
    public void setWeeklyMenuService(WeeklyMenuServiceI weeklyMenuService) {
        this.weeklyMenuService = weeklyMenuService;
    }
}