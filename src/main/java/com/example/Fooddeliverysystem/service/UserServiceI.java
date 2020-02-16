package com.example.Fooddeliverysystem.service;

import com.example.Fooddeliverysystem.dto.UserDTO;
import com.example.Fooddeliverysystem.model.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserServiceI {
    User registerUser(User user);

    User confirmUserAccount(String token);

    UserDTO getUser(int userId);

    List<UserDTO> getAllUsers();

    boolean updateUser(int userId, String command);

    ResponseEntity<String> updateUserEmail(Integer id, String email);

    ResponseEntity<String> updateUserImage(Integer id, String imagePath);

    ResponseEntity<String> updateUserPassword(String token, String password);

    UserDTO getUserByEmail(String email);

    User resetPassword(String email);
}