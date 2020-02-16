package com.example.Fooddeliverysystem.controller;

import com.example.Fooddeliverysystem.dto.TokenDTO;
import com.example.Fooddeliverysystem.model.User;
import com.example.Fooddeliverysystem.service.NotificationServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("notification")
public class NotificationController {

    private NotificationServiceI notificationServiceI;

    @CrossOrigin
    @PostMapping("/token")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> saveTokenForNotification(@RequestBody TokenDTO token,
                                                           @RequestHeader("Authorization") String authorization) throws IOException {
        User user = notificationServiceI.getUser(authorization);
        if (user == null) {
            return new ResponseEntity<>("You must sign in to be able to order meals.", HttpStatus.BAD_REQUEST);
        }
        if (notificationServiceI.saveToken(token.getToken(), user)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Autowired
    public void setNotificationServiceI(NotificationServiceI notificationServiceI) {
        this.notificationServiceI = notificationServiceI;
    }
}