package com.example.Fooddeliverysystem.repository;

import com.example.Fooddeliverysystem.model.Confirmationtoken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfirmationTokenRepo extends JpaRepository<Confirmationtoken, Integer> {
    Confirmationtoken findByConfirmationToken(String token);
}