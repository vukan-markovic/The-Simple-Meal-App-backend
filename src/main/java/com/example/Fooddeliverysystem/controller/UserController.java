package com.example.Fooddeliverysystem.controller;

import com.example.Fooddeliverysystem.dto.UserDTO;
import com.example.Fooddeliverysystem.service.UserServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("user")
public class UserController {

    private UserServiceI userService;

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserDTO> getUser(@PathVariable("id") Integer id) {
        UserDTO user = userService.getUser(id);
        if (user == null)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/all")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> result = userService.getAllUsers();
        if (result.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/email/{mail}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserDTO> getUserByEmail(@PathVariable("mail") String email) {
        UserDTO userDTO = userService.getUserByEmail(email);
        if (userDTO == null)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @CrossOrigin
    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> updateUserEmail(@PathVariable("id") Integer id, @RequestBody String email) {
        return userService.updateUserEmail(id, email);
    }

    @CrossOrigin
    @PutMapping("/{id}/{command}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> update(@PathVariable("id") Integer id, @PathVariable("command") String command) {
        if (userService.updateUser(id, command))
            return new ResponseEntity<>(HttpStatus.ACCEPTED);

        return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @CrossOrigin
    @PutMapping("/image/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> updateUserImage(@PathVariable("id") Integer id, @RequestBody String imagePath) {
        return userService.updateUserImage(id, imagePath);
    }

    @CrossOrigin
    @PutMapping("/updatePassword/{token}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> updateUserPassword(@PathVariable("token") String token, @RequestBody String password) {
        return userService.updateUserPassword(token, password);
    }

    @Autowired
    public void setUserService(UserServiceI userService) {
        this.userService = userService;
    }
}