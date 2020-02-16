package com.example.Fooddeliverysystem.service;

import com.example.Fooddeliverysystem.model.Type;

import java.util.Collection;
import java.util.List;

public interface TypeServiceI {
    Type getType(Integer id);

    Collection<Type> getAllTypes();

    void deleteType(Integer id);

    Type insertType(Type type);

    Type updateType(Type type);


    Collection<Type> getRegularTypes();

    Collection<Type> getTypesWithRegular();

    Collection<Type> getTypesWithoutRegular();

    List<Type> updateRegularTypes(List<Type> types);

}