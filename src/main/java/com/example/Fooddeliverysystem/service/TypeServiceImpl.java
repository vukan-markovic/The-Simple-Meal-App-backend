package com.example.Fooddeliverysystem.service;

import com.example.Fooddeliverysystem.model.Type;
import com.example.Fooddeliverysystem.repository.TypeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class TypeServiceImpl implements TypeServiceI {

    private TypeRepo typeRepo;

    private JdbcTemplate jdbcTemplate;

    @Override
    public Collection<Type> getAllTypes() {
        return typeRepo.findAll();
    }

    @Override
    public Type getType(Integer id) {
        return typeRepo.findByTypeId(id);
    }

    @Override
    public Type insertType(Type type) {
        Type result = typeRepo.findByNameIgnoreCaseAndPriceAndRegular(type.getName(),type.getPrice(), type.isRegular());
        if(result!=null){
            System.out.println(result);
            return null;
        }
        return typeRepo.save(type);
    }

    @Override
    public Type updateType(Type type) {


        Type newType = typeRepo.findByTypeId(type.getTypeId());
        if (newType == null) {
            return null;
        }
        Type result = typeRepo.findByNameIgnoreCaseAndPriceAndRegular(type.getName(),type.getPrice(), type.isRegular());

        if(result==null){

            newType.setName(type.getName());
            newType.setPrice(type.getPrice());
            newType.setRegular(type.isRegular());

            System.out.println(type.getName());
            System.out.println(type.getPrice());
            System.out.println(type.isRegular());

            return typeRepo.save(newType);
        }

        return null;
    }

    @Override
    public void deleteType(Integer id) {
        jdbcTemplate.execute("delete from mealtype where Type_typeId = " + id);
        jdbcTemplate.execute("delete from mealtypeuserorder where MealType_Type_typeId = " + id);
        typeRepo.deleteById(id);
    }

    @Override
    public Collection<Type> getRegularTypes() {
        return typeRepo.findAllByRegularTrue();
    }

//////////////////////////////////////////OLD///////////////////////////////////////////////////////////////////////


    @Override
    public List<Type> updateRegularTypes(List<Type> types) {
        List<Type> newTypes = new ArrayList<>();
        if (types == null || types.isEmpty()) {
            return newTypes;
        }
        for (Type type : types) {
            Type newType = typeRepo.findByTypeId(type.getTypeId());
            if (newType == null) {
                return new ArrayList<>();
            }
            if (type.getPrice() < 1) {
                return new ArrayList<>();
            }
            newType.setPrice(type.getPrice());
            newType = typeRepo.save(newType);
            newTypes.add(newType);
        }
        return newTypes;
    }

    @Override
    public Collection<Type> getTypesWithoutRegular() {
        Collection<Type> types = typeRepo.findAll();
        if (types.isEmpty()) {
            return null;
        } else {
            removeSmallAndBig(types);
        }
        return types;
    }

    private void removeSmallAndBig(Collection<Type> types) {
        for (Type type : types) {
            String typeName = type.getName().toLowerCase();
            if (typeName.equals("small")) {
                types.remove(type);
                break;
            }
        }
        for (Type type : types) {
            String typeName = type.getName().toLowerCase();
            if (typeName.equals("big")) {
                types.remove(type);
                break;
            }
        }
    }

    @Override
    public Collection<Type> getTypesWithRegular() {
        Collection<Type> types = typeRepo.findAll();
        if (types.isEmpty()) {
            return null;
        } else {
            removeSmallAndBig(types);
            Type regular = new Type();
            regular.setName("regular");
            regular.setPrice(0);
            types.add(regular);
        }
        return types;
    }

    @Autowired
    public void setTypeRepo(TypeRepo typeRepo) {
        this.typeRepo = typeRepo;
    }

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}