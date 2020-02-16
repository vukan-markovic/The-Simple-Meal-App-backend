package com.example.Fooddeliverysystem.controller;

import com.example.Fooddeliverysystem.model.ViberSender;
import com.example.Fooddeliverysystem.service.ViberServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("viber")
public class ViberController {

    private ViberServiceI viberService;

    @Autowired
    public void setViberService(ViberServiceI viberService) {
        this.viberService = viberService;
    }

    @CrossOrigin
    @PostMapping
    public ResponseEntity<ViberSender> insertUserId(@RequestBody String userId) {
        ViberSender viberSender = viberService.insert(userId);
        return new ResponseEntity<>(viberSender, HttpStatus.OK);
    }
}