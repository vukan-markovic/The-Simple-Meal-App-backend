package com.example.Fooddeliverysystem.controller;

import com.example.Fooddeliverysystem.dto.MealTypeDTO;
import com.example.Fooddeliverysystem.dto.UserDTO;
import com.example.Fooddeliverysystem.dto.UserIdsDTO;
import com.example.Fooddeliverysystem.service.ChosenOneService;
import com.example.Fooddeliverysystem.service.ChosenOneServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("chosenOne")
public class ChosenOneController {


    private ChosenOneServiceI chosenOneService;

    @PostMapping("setPaid")
    @PreAuthorize("hasAuthority('CHOSEN')")
    public ResponseEntity setPaid(@Valid @RequestBody MealTypeDTO mealTypeDTO) {
        try {
            chosenOneService.setPaid(mealTypeDTO);
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("getChosenOne")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity getChosenOne() {
        UserDTO userDTO = this.chosenOneService.getChosenOne();

        return userDTO != null ? new ResponseEntity<>(userDTO, HttpStatus.OK) : new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PostMapping("payingNotification")
    @PreAuthorize("hasAuthority('CHOSEN')")
    public ResponseEntity payingNotification(@RequestBody UserIdsDTO userIdsDTO){
        try {
            chosenOneService.payingNotification(userIdsDTO.getUserIds());
            return new ResponseEntity(HttpStatus.OK);
        } catch (IllegalArgumentException | IllegalStateException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }


    @Autowired
    public void setChosenOneService(ChosenOneServiceI chosenOneService) {
        this.chosenOneService = chosenOneService;
    }

}