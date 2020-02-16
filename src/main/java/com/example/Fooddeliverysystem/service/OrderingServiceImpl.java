package com.example.Fooddeliverysystem.service;

import com.example.Fooddeliverysystem.dto.MealOrderingDTO;
import com.example.Fooddeliverysystem.dto.MealTypeDTO;
import com.example.Fooddeliverysystem.dto.UserDTO;
import com.example.Fooddeliverysystem.model.*;
import com.example.Fooddeliverysystem.repository.*;
import com.example.Fooddeliverysystem.security.TokenUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class OrderingServiceImpl implements OrderingServiceI {

    private MealRepo mealRepo;
    private TypeRepo typeRepo;
    private UserRepo userRepo;
    private UserorderRepo userorderRepo;
    private MealTypeRepo mealTypeRepo;

    private TokenUtils tokenUtils;
    private Environment environment;

    @Autowired
    public void setEnvironment(Environment environment) { this.environment = environment; }

    @Override
    public User getUser(String token) {
        if (token.isEmpty()) {
            return null;
        }
        String username = tokenUtils.getUsernameFromToken(token);
        return userRepo.findByEmail(username);
    }


    @Override
    public List<Userorder> ordering(List<MealOrderingDTO> meals, User user) throws NumberFormatException {
        Date dateToday = new Date();
       if (!checkDate(dateToday)) {
            return null;
        }
        ArrayList<Userorder> userOrders = new ArrayList<>();
        Date todayTen = getDate(new Date(), orderingOrderUntilHour(),orderingOrderUntilMin(),0);
        Date dateTomorrow = getDateForTomorrow(dateToday);

        for (MealOrderingDTO mealOrderingCurrent : meals) {
            MealOrderingDTO mealOrdering = new MealOrderingDTO();
            ModelMapper modalMapper = new ModelMapper();
            modalMapper.map(mealOrderingCurrent, mealOrdering);

            Meal meal = mealRepo.getByMealId(mealOrdering.getMeal().getMealId());
            Type type;

            if (meal.getTypes().get(0).isRegular()) {
                type = meal.getType(mealOrdering.getRegular());
            } else {
                type = meal.getType();
            }
            if (type == null) {
                return userOrders;
            }
            if (meal.getEarlyOrder()) {
                //for tommorow
                MealType mealTypeTomorrow = getMealType(meal, type);
                if (mealTypeTomorrow == null) {
                    return userOrders;
                }
                Userorder tomorrow;
                for (int i = 0; i < mealOrdering.getCount(); i++) {
                    tomorrow = getUserOrder(userRepo.findByUserId(user.getUserId()), dateTomorrow);
                    tomorrow.getMealtypes().add(mealTypeTomorrow);
                    mealTypeTomorrow.addUserOrder(tomorrow);
                    userOrders.add(userorderRepo.save(tomorrow));
                }
                mealTypeRepo.save(mealTypeTomorrow);
            } else {

                if (dateToday.after(todayTen)) {
                    return null;
                }
                //for today
                MealType mealType1 = getMealType(meal, type);
                if (mealType1 == null) {
                    return new ArrayList<>();
                }
                Userorder today;
                for (int i = 0; i < mealOrdering.getCount(); i++) {
                    today = getUserOrder(user, dateToday);
                    today.getMealtypes().add(mealType1);
                    mealType1.addUserOrder(today);
                    userOrders.add(userorderRepo.save(today));
                }
                mealTypeRepo.save(mealType1);
            }
        }
        return userOrders;
    }

    @Override
    public List<MealTypeDTO> getAllOrders(String forDay) {
        List<MealTypeDTO> allOrders = new ArrayList<>();

        for (User user : userRepo.findAll()) {
            System.out.println(user.getEmail());
            allOrders.addAll(this.getOrdering(forDay, user));
        }

        return allOrders;
    }

    @Override
    public List<MealTypeDTO> getOrdering(String forDay, User user) {

        Date date;
        if (forDay.equals("today")) {
            date = new Date();
        } else {
            date = getDateForTomorrow(new Date());
        }

        List<MealTypeDTO> mealTypeDTOS = new ArrayList<>();

        List<Userorder> userorders = userorderRepo.findByUserUserIdAndDate(user.getUserId(), date);

        for (Userorder userorder : userorders) {
            for (MealType mealType : userorder.getMealtypes()) {
                MealTypeDTO mealTypeDTO = new MealTypeDTO();
                mealTypeDTO.setMeal(mealRepo.getByMealId(mealType.getId().getMeal_mealId()));
                mealTypeDTO.setType(typeRepo.findByTypeId(mealType.getId().getType_typeId()));
                mealTypeDTO.setUserOrderId(userorder.getUserOrderId());
                mealTypeDTO.setPaid(userorder.getPaid());
                UserDTO userDTO = new UserDTO();
                userDTO.setEmail(userorder.getUser().getEmail());
                userDTO.setName(userorder.getUser().getName());
                userDTO.setLastName(userorder.getUser().getLastName());
                userDTO.setPathImage(userorder.getUser().getPathImage());
                userDTO.setRole(userorder.getUser().getRole().getName());
                userDTO.setUserId(userorder.getUser().getUserId());
                mealTypeDTO.setUser(userDTO);

                mealTypeDTOS.add(mealTypeDTO);
            }
        }
        return mealTypeDTOS;
    }

    private Date getDate(Date date, int hour, int min, int sec){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.SECOND, sec);
        Date newDate=  calendar.getTime();
        return newDate;
    }

    private boolean checkDate(Date dateForCheck) {
        if (dateForCheck.after(getDate(new Date(),orderingOrderUntilHour(),orderingOrderUntilMin(),0))) {
            return !dateForCheck.after( getDate(new Date(),orderingEarlyOrderUntilHour(),orderingEarlyOrderUntilMin(),0));
        }
        return true;
    }
    public Date getDateForTomorrow(Date dateToday){
        Calendar c = Calendar.getInstance();
        c.setTime(dateToday);
        c.add(Calendar.DATE, 1);
        return c.getTime();
    }

    private boolean checkDateForDeleting(Date dateForCheck) throws NumberFormatException{
        Date today = new Date();

        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.add(Calendar.DATE, 1);
        Date dateTomorrow = c.getTime();

        if(today.after(getDate(new Date(),orderingOrderUntilHour(),orderingOrderUntilMin(),0)) && dateForCheck.before(dateTomorrow)){
           return false;
        }else {
            return !today.after(getDate(new Date(),orderingEarlyOrderUntilHour(),orderingEarlyOrderUntilMin(),0));
        }
    }


    @Override
    public boolean deleteOrder(int userorderId, User user) throws NumberFormatException{
        Userorder userorder = userorderRepo.findByUserOrderId(userorderId);
        try{
            if (!checkDateForDeleting(userorder.getDate())) {
                return false;
            }
        }catch (NumberFormatException e){

        }

        if (userorder == null) {
            return false;
        }
        for (MealType mealType : userorder.getMealtypes()) {
            mealType.removeUserOrder(userorder);
            mealTypeRepo.save(mealType);
        }
        user.removeUserorder(userorder);
        userRepo.save(user);

        userorderRepo.deleteById(userorderId);

        return true;
    }

    private MealType getMealType(Meal meal, Type type) {
        MealtypePK mealtypePK = new MealtypePK();
        mealtypePK.setMeal_mealId(meal.getMealId());
        mealtypePK.setType_typeId(type.getTypeId());
        return mealTypeRepo.findById(mealtypePK);

    }

    private Userorder getUserOrder(User user, Date date) {
        Userorder userorder = new Userorder();
        userorder.setDate(date);
        userorder.setPaid(false);
        userorder.setUser(user);
        user.addUserorder(userorder);
        userRepo.save(user);
        return userorderRepo.save(userorder);
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
    public void setUserRepo(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Autowired
    public void setUserorderRepo(UserorderRepo userorderRepo) {
        this.userorderRepo = userorderRepo;
    }

    @Autowired
    public void setMealTypeRepo(MealTypeRepo mealTypeRepo) {
        this.mealTypeRepo = mealTypeRepo;
    }

    @Autowired
    public void setTokenUtils(TokenUtils tokenUtils) {
        this.tokenUtils = tokenUtils;
    }

    private int orderingOrderUntilHour() {
        return Integer.parseInt(environment.getProperty("ordering.order.until.hour"));
    }

    private int orderingOrderUntilMin() {
        return Integer.parseInt(environment.getProperty("ordering.order.until.min"));
    }


    private int orderingEarlyOrderUntilHour() {
        return Integer.parseInt(environment.getProperty("ordering.early.order.until.hour"));
    }

    private int orderingEarlyOrderUntilMin() {
        return Integer.parseInt(environment.getProperty("ordering.early.order.until.min"));
    }
}
