package com.example.Fooddeliverysystem.repository;

import com.example.Fooddeliverysystem.model.Role;
import com.example.Fooddeliverysystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {
    User findByEmailAndStatus(String userEmail, int status);

    User findByEmail(String email);

    User findByRole(Role role);

    List<User> findAllByRole(Role role);

    User findByToken(String token);

    @Query(" from User u  where u.userId = :userId")
    User findByUserId(int userId);

    User findByUserIdAndStatus(int userId, int status);
}