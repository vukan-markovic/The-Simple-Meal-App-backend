package com.example.Fooddeliverysystem.repository;

import com.example.Fooddeliverysystem.model.Dailymenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
@Repository
public interface DailyMenuRepo extends JpaRepository<Dailymenu, Integer> {

    Dailymenu findByDate(Date date);

    Dailymenu findByDailyMenuId(int id);
}