package com.example.Fooddeliverysystem.repository;

import com.example.Fooddeliverysystem.model.Userorder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface UserorderRepo extends JpaRepository<Userorder, Integer> {

    List<Userorder> findByUserUserIdAndDate(int id, Date date);

    Userorder findByUserOrderId(int id);

    List<Userorder> findAllByDate(Date date);

}