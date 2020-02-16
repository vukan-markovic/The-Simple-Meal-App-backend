package com.example.Fooddeliverysystem.service;

import com.example.Fooddeliverysystem.dto.AddMealDTO;
import com.example.Fooddeliverysystem.dto.MealEditDTO;
import com.example.Fooddeliverysystem.model.Meal;
import com.example.Fooddeliverysystem.model.MealType;
import com.example.Fooddeliverysystem.model.MealtypePK;
import com.example.Fooddeliverysystem.model.Type;
import com.example.Fooddeliverysystem.repository.MealRepo;
import com.example.Fooddeliverysystem.repository.MealTypeRepo;
import com.example.Fooddeliverysystem.repository.TypeRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MealServiceImpl implements MealIServiceI {
    private MealRepo mealRepo;
    private TypeRepo typeRepo;
    private MealTypeRepo mealTypeRepo;

//    private Meal addRegularMeal(MealDTO mealDto) {
//        List<Type> small = typeRepo.findByNameIgnoreCase("small");
//        if(small == null || small.size() > 1){
//            return null;
//        }
//        List<Type> big = typeRepo.findByNameIgnoreCase("big");
//
//        if(big == null || big.size() > 1){
//            return null;
//        }
//        Type smallType =  typeRepo.findByTypeId(small.get(0).getTypeId());
//        Type bigType =  typeRepo.findByTypeId(big.get(0).getTypeId());
//
//        Meal meal = mealDto.toMeal();
//        meal.setIsRegular(true);
//
//
//        Meal newMeal = mealRepo.save(meal);
//
//        MealtypePK mealtypePK = new MealtypePK();
//        mealtypePK.setType_typeId(smallType.getTypeId());
//        mealtypePK.setMeal_mealId(newMeal.getMealId());
//        MealType mealType = new MealType();
//        mealType.setId(mealtypePK);
//
//        mealTypeRepo.save(mealType);
//
//        MealtypePK mealtypePK1 = new MealtypePK();
//        mealtypePK1.setType_typeId(bigType.getTypeId());
//        mealtypePK1.setMeal_mealId(newMeal.getMealId());
//        MealType mealType1 = new MealType();
//        mealType1.setId(mealtypePK1);
//
//        mealTypeRepo.save(mealType1);
//
//        return newMeal;
//    }

//    @Override
//    public Meal addMeal(MealDTO mealDto1) {
//        if(mealDto1 == null){
//            return null;
//        }
//        ModelMapper modalMapper = new ModelMapper();
//
//        MealDTO mealDto = new MealDTO();
//
//        modalMapper.map(mealDto1,mealDto);
//
//        if(mealDto.getType().getName().equals("regular")) {
//
//            return addRegularMeal(mealDto);
//
//        }else {
//            if(mealDto.getPrice() == null) {
//
//                return null;
//
//            }
//            Meal meal = mealDto.toMeal();
//            meal.setIsRegular(false);
//            Type type = checkType(mealDto.getType(),mealDto.getPrice());
//
//            if(type == null){
//                return null;
//            }
//
//            Meal newMeal = mealRepo.save(meal);
//
//            MealtypePK mealtypePK = new MealtypePK();
//            mealtypePK.setType_typeId(type.getTypeId());
//            mealtypePK.setMeal_mealId(newMeal.getMealId());
//            MealType mealType = new MealType();
//            mealType.setId(mealtypePK);
//
//            mealTypeRepo.save(mealType);
//
//            return newMeal;
//        }
//
//    }

    @Override
    public Meal insertMeal(AddMealDTO addMealDTO) {
        Meal meal = mealRepo.save(addMealDTO.getMeal());

        if (meal == null)
            return null;

        for (Type type : addMealDTO.getTypes()) {
            Type newType = typeRepo.findByNameIgnoreCaseAndPrice(type.getName(), type.getPrice());

            if (newType == null) {
                Type insertType = new Type();
                insertType.setName(newType.getName());
                insertType.setPrice(newType.getPrice());
                insertType.setRegular(newType.isRegular());
                newType = typeRepo.save(insertType);
            }

            MealtypePK mealtypePK = new MealtypePK();
            mealtypePK.setType_typeId(newType.getTypeId());
            mealtypePK.setMeal_mealId(meal.getMealId());
            MealType mealType = new MealType();
            mealType.setId(mealtypePK);

            mealTypeRepo.save(mealType);
        }

        return meal;
    }

    @Override
    public Meal editMeal(MealEditDTO mealEditDTO1) {
        ModelMapper modalMapper = new ModelMapper();

        MealEditDTO mealEditDTO = new MealEditDTO();
        modalMapper.map(mealEditDTO1, mealEditDTO);

        if (!mealRepo.existsById(mealEditDTO.getId())) {
            return null;
        }
        Meal oldMeal = mealRepo.getByMealId(mealEditDTO.getId());
        oldMeal.setName(mealEditDTO.getName());
        oldMeal.setDescription(mealEditDTO.getDescription());
        oldMeal.setEarlyOrder(mealEditDTO.isEarlyorder());


        return mealRepo.save(oldMeal);
    }

    @Override
    public Meal getMeal(Integer id) {

        return mealRepo.getByMealId(id);
    }

    @Override
    public boolean deleteMeal(int id) {
        if (!mealRepo.existsById(id)) {
            return false;
        }
        //TODO:
        /*
        jdbcTemplate.execute("delete from Type where atibut =" + id);
        jdbcTemplate.execute("delete from Naziv where atibut =" + id);
        jdbcTemplate.execute("delete from Naziv where atibut =" + id);*/

        mealRepo.deleteById(id);
        return true;
    }

    @Override
    public List<Meal> getMeals() {
        return mealRepo.findAll();
    }

    private Type checkType(Type type, Double price) {

        Type existType = typeRepo.findByNameIgnoreCaseAndPrice(type.getName(), price);

        if (existType == null) {
            Type newType = new Type();
            newType.setName(type.getName());
            newType.setPrice(price);

            return typeRepo.save(newType);
        }
        return existType;
    }


    @Autowired
    public void setMealRepo(MealRepo mealRepo) {
        this.mealRepo = mealRepo;
    }

    @Autowired
    public void setTypeRepo(TypeRepo typeRepo) {
        this.typeRepo = typeRepo;
    }

    @Autowired
    public void setMealTypeRepo(MealTypeRepo mealTypeRepo) {
        this.mealTypeRepo = mealTypeRepo;
    }
}