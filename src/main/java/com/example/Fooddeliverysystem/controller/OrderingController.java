package com.example.Fooddeliverysystem.controller;

import com.example.Fooddeliverysystem.dto.MealOrderingDTO;
import com.example.Fooddeliverysystem.dto.MealTypeDTO;
import com.example.Fooddeliverysystem.model.User;
import com.example.Fooddeliverysystem.model.Userorder;
import com.example.Fooddeliverysystem.service.OrderingServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("ordering")
public class OrderingController {

    private OrderingServiceI orderingServiceI;

    @CrossOrigin
    @PostMapping()
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Userorder>> ordering(@Valid @RequestBody List<MealOrderingDTO> meals, @RequestHeader("Authorization") String token) {
        User user = orderingServiceI.getUser(token);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }

       List<Userorder> userOrders =null;
        try {
            userOrders = orderingServiceI.ordering(meals, user);
        }catch (NumberFormatException e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if(userOrders == null) {
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }else if (userOrders.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(userOrders,HttpStatus.OK);
        }
    }

    @CrossOrigin
    @DeleteMapping("/{userorderId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> deleteOrder(@PathVariable("userorderId") Integer userorderId,
                                              @RequestHeader("Authorization") String token) {
        User user = orderingServiceI.getUser(token);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
        try {
            if (orderingServiceI.deleteOrder(userorderId, user)) {
                return new ResponseEntity<>(HttpStatus.ACCEPTED);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
            }
        }catch (NumberFormatException e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("allOrders")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity getAllOrders(@RequestParam String forDay) {
        List<MealTypeDTO> mealTypeDTOS = orderingServiceI.getAllOrders(forDay);
        System.out.println(mealTypeDTOS.size());
        if (mealTypeDTOS.isEmpty()) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(mealTypeDTOS, HttpStatus.OK);
    }

    @GetMapping("/all")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<MealTypeDTO>> getOrdering(@RequestParam("forDay") String forDay,
                                                         @RequestHeader("Authorization") String token) {
        //if forDay is today you will get ordering for today
        // if forDay is tomorrow you will get ordering for tomorrow
        User user = orderingServiceI.getUser(token);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<MealTypeDTO> result = orderingServiceI.getOrdering(forDay, user);
        if (result.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok().body(result);
    }

    @Autowired
    public void setOrderingServiceI(OrderingServiceI orderingServiceI) {
        this.orderingServiceI = orderingServiceI;
    }
}