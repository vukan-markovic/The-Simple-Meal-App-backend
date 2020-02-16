package com.example.Fooddeliverysystem.repository;

import com.example.Fooddeliverysystem.model.MealType;
import com.example.Fooddeliverysystem.model.MealtypePK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MealTypeRepo extends JpaRepository<MealType, Integer> {
    MealType findById(MealtypePK mealtypePK);
}