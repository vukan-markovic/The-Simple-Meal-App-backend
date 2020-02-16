package com.example.Fooddeliverysystem.controller;

import com.example.Fooddeliverysystem.dto.DailyMenuDTO;
import com.example.Fooddeliverysystem.dto.DailyMenuForUpdateDTO;
import com.example.Fooddeliverysystem.model.Dailymenu;
import com.example.Fooddeliverysystem.service.DailyMenuServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Date;

@RestController
@RequestMapping("daily-menu")
public class DailyMenuController {

    private DailyMenuServiceI dailyMenuService;

    @GetMapping()
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Dailymenu> getDailyMenu() {
        Dailymenu menu = null;
        try{
            menu = dailyMenuService.getDailyMenuForToday();
        }catch (NumberFormatException e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (menu != null) {
            return new ResponseEntity<>(menu, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @CrossOrigin
    @PostMapping()
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Dailymenu> saveDailyMenu(@Valid @RequestBody DailyMenuDTO menuDTO) {
        Dailymenu dailymenu = null;

        try{
            dailymenu = dailyMenuService.saveDailyMenu(menuDTO);
        }catch (NumberFormatException e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if ( dailymenu != null) {
            return new ResponseEntity<>(dailymenu, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @CrossOrigin
    @PutMapping()
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Dailymenu> updateDailyMenu(@Valid @RequestBody DailyMenuForUpdateDTO dailyMenuForUpdateDTO) {
        Dailymenu dailymenu = null;
        try {
            dailymenu = dailyMenuService.updateDailyMenu(dailyMenuForUpdateDTO);
        }catch (NumberFormatException e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (dailymenu== null)
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);

        return new ResponseEntity<>(dailymenu,HttpStatus.OK);
    }

    @GetMapping("/all/{weaklyMenuId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Collection<Dailymenu>> getDailyMenu(@PathVariable("weaklyMenuId") Integer weaklyMenuId) {
        Collection<Dailymenu> menu = dailyMenuService.getDailyMenus(weaklyMenuId);

        if (menu != null || !menu.isEmpty()) {
            return new ResponseEntity<>(menu, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @GetMapping("/days/{weaklyMenuId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Collection<Date>> getDays(@PathVariable("weaklyMenuId") Integer weaklyMenuId) {
        Collection<Date> result = dailyMenuService.getDays(weaklyMenuId);
        if (result.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Autowired
    public void setDailyMenuService(DailyMenuServiceI dailyMenuService) {
        this.dailyMenuService = dailyMenuService;
    }
}