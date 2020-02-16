package com.example.Fooddeliverysystem.repository;

import com.example.Fooddeliverysystem.model.Weaklymenu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Date;
import java.util.List;

public interface WeeklyMenuRepo extends JpaRepository<Weaklymenu, Integer> {
    Weaklymenu findByDateFromLessThanEqualAndDateToGreaterThanEqual(Date today, Date today2);
    Weaklymenu findByDateFromAndAndDateTo(Date from, Date to);
    Weaklymenu findByWeaklyMenuId(int id);
}