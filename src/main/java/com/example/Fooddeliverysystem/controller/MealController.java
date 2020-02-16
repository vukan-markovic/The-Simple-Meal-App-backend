package com.example.Fooddeliverysystem.controller;

import com.example.Fooddeliverysystem.dto.AddMealDTO;
import com.example.Fooddeliverysystem.dto.MealEditDTO;
import com.example.Fooddeliverysystem.model.Meal;
import com.example.Fooddeliverysystem.service.MealIServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("meal")
public class MealController {

    private MealIServiceI mealService;

    @CrossOrigin
    @PostMapping()
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Meal> addMeal(@Valid @RequestBody AddMealDTO addMealDTO) {
        Meal meal = mealService.insertMeal(addMealDTO);
        if (meal != null) {
            return new ResponseEntity<>(meal,HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    //TODO: Implement method deleteMeal
    @CrossOrigin
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteMeal(@PathVariable("id") Integer id) {
        if (mealService.deleteMeal(id)) {
            return new ResponseEntity<>("You have successfully removed the meal.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Error occurred while removing a meal.", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/all")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Meal>> getMeals() {
        List<Meal> result = mealService.getMeals();
        if (result.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Meal> getMeal(@PathVariable("id") Integer id) {
        Meal meal = mealService.getMeal(id);
        if (meal == null)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return ResponseEntity.ok().body(meal);
    }

    @CrossOrigin
    @PutMapping()
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Meal> updateMeal(@Valid @RequestBody MealEditDTO meal) {
        Meal newMeal = mealService.editMeal(meal);
        if (newMeal != null) {
            return new ResponseEntity<>(newMeal, HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>( HttpStatus.NOT_MODIFIED);
        }
    }

    @Autowired
    public void setMealService(MealIServiceI mealService) {
        this.mealService = mealService;
    }
}