package com.example.Fooddeliverysystem.repository;

import com.example.Fooddeliverysystem.model.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TypeRepo extends JpaRepository<Type, Integer> {
    Type findByNameIgnoreCaseAndPrice(String name, double price);
    Type findByNameIgnoreCaseAndPriceAndRegular(String name, double price, boolean regular);

    List<Type> findByNameIgnoreCase(String name);

    Type findByTypeId(int id);

    List<Type> findAllByRegularTrue();


    @Query(value = "SELECT * FROM type  WHERE UPPER(name)= UPPER(:name) AND price=:price AND regular=:regular ",nativeQuery = true)
    Type checkType(@Param("name") String name, @Param("price")  Double price, @Param("regular") Boolean regular);
}