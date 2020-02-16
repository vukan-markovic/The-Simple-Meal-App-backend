package com.example.Fooddeliverysystem.repository;

import com.example.Fooddeliverysystem.model.ViberSender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ViberSenderRepo extends JpaRepository<ViberSender, Integer> {

    @Query("select distinct  v from ViberSender v group by v.userId")
    List<ViberSender> findDistinct();
}