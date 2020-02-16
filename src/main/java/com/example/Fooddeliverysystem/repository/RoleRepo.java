package com.example.Fooddeliverysystem.repository;

import com.example.Fooddeliverysystem.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepo extends JpaRepository<Role, Integer> {
    Role findByNameIgnoreCase(String roleName);
}