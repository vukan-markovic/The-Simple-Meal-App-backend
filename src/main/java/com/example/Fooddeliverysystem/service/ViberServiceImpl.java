package com.example.Fooddeliverysystem.service;

import com.example.Fooddeliverysystem.dto.MealTypeDTO;
import com.example.Fooddeliverysystem.model.Meal;
import com.example.Fooddeliverysystem.model.MealType;
import com.example.Fooddeliverysystem.model.Userorder;
import com.example.Fooddeliverysystem.model.ViberSender;
import com.example.Fooddeliverysystem.repository.MealRepo;
import com.example.Fooddeliverysystem.repository.TypeRepo;
import com.example.Fooddeliverysystem.repository.UserorderRepo;
import com.example.Fooddeliverysystem.repository.ViberSenderRepo;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ViberServiceImpl implements ViberServiceI {

    private UserorderRepo userorderRepo;

    private MealRepo mealRepo;

    private TypeRepo typeRepo;

    private ViberSenderRepo viberSenderRepo;

    private Environment environment;

    @Autowired
    public void setUserorderRepo(UserorderRepo userorderRepo) {
        this.userorderRepo = userorderRepo;
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
    public void setViberSenderRepo(ViberSenderRepo viberSenderRepo) {
        this.viberSenderRepo = viberSenderRepo;
    }

    @Autowired
    public void setEnvironment(Environment environment) { this.environment = environment; }

    private static final String VIBER_API_URL = "https://chatapi.viber.com/pa/broadcast_message";
    private static final String HEADER_NAME = "X-Viber-Auth-Token";


    private void sendMessage(String messageToSend) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(HEADER_NAME, this.viberXViberAuthTokenValue());

        List<ViberSender> result = viberSenderRepo.findDistinct();

        List<String> broadcastList = new LinkedList<>();
        for (ViberSender viberSender : result) {
            broadcastList.add(viberSender.getUserId());
        }

        JSONObject message = new JSONObject();

        JSONObject sender = new JSONObject();
        sender.put("name", "Simple meal");
        sender.put("avatar", "http://avatar.example.com");
        message.put("sender", sender);

        message.put("min_api_version", "2");
        message.put("type", "text");
        message.put("text", messageToSend);

        message.put("broadcast_list", broadcastList);

        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<JSONObject> entity = new HttpEntity<>(message, headers);
        restTemplate.postForEntity(VIBER_API_URL, entity, JSONObject.class);
    }

    @Override
    public ViberSender insert(String userId) {
        ViberSender viberSender = new ViberSender();
        viberSender.setUserId(userId);
        viberSender.setTime(new Date());
        return viberSenderRepo.save(viberSender);
    }


    @Scheduled(cron = "${viber.timer.generateEventForNormalOrders}")
    private void generateEventForOrdersAt10PM() {


        List<MealTypeDTO> mealTypeDTOs = getOrders(false, new Date());
        Map<MealTypeDTO, Integer> frequencyMap = mealCounter(mealTypeDTOs);

        String message = generateMessage(frequencyMap, false);

        if (message == null) {
            return;
        }

        sendMessage(message);
    }


    @Scheduled(cron = "${viber.timer.generateEventForEarlyOrders}")
    private void generateEventForEarlyOrders() {

        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, 1);
        List<MealTypeDTO> mealTypeDTOs = getOrders(true, c.getTime());
        Map<MealTypeDTO, Integer> frequencyMap = mealCounter(mealTypeDTOs);
        String message = generateMessage(frequencyMap, true);

        if (message == null) {
            return;
        }
        sendMessage(message);
    }

    private String getDayName(int day) {
        switch (day) {
            case 1:
                return "Sunday";
            case 2:
                return "Monday";
            case 3:
                return "Tuesday";
            case 4:
                return "Wednesday";
            case 5:
                return "Thursday";
            case 6:
                return "Friday";
            case 7:
                return "Saturday";
        }
        return "";
    }

    private String generateMessage(Map<MealTypeDTO, Integer> frequencyMap, boolean earlyOrder) {
        if (frequencyMap.isEmpty()) {
            return null;
        }

        StringBuilder message = new StringBuilder();
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        if (earlyOrder) {
            calendar.add(Calendar.DATE, 1);

            String day = getDayName(calendar.get(Calendar.DAY_OF_WEEK));
            message.append("Early order for " + day + " \n");
            message.append("\n");
        } else {
            String day = getDayName(calendar.get(Calendar.DAY_OF_WEEK));
            message.append("Order for " + day + " \n");
            message.append("\n");
        }

        for (Map.Entry<MealTypeDTO, Integer> entry : frequencyMap.entrySet()) {
            message.append(entry.getKey() + " x  " + entry.getValue() + " \n");
            message.append("\n");
        }

        message.append(this.viberMessageFooter());

        return message.toString();
    }


    private Map<MealTypeDTO, Integer> mealCounter(List<MealTypeDTO> mealTypeDTOs) {
        Map<MealTypeDTO, Integer> frequencyMap = new HashMap<>();
        for (MealTypeDTO dto : mealTypeDTOs) {
            if (!frequencyMap.containsKey(dto))
                frequencyMap.put(dto, 1);
            else
                frequencyMap.put(dto, frequencyMap.get(dto) + 1);
        }
        return frequencyMap;

    }

    private List<MealTypeDTO> getOrders(boolean earlyOrder, Date date) {
        List<Userorder> userOrders = userorderRepo.findAllByDate(date);
        List<MealTypeDTO> mealTypeDTOs = new LinkedList<>();

        for (Userorder userorder : userOrders) {
            for (MealType mealType : userorder.getMealtypes()) {
                Meal meal = mealRepo.getByMealId(mealType.getId().getMeal_mealId());

                if (earlyOrder) {
                    if (!meal.getEarlyOrder())
                        continue;
                } else {
                    if (meal.getEarlyOrder())
                        continue;
                }

                MealTypeDTO mealTypeDTO = new MealTypeDTO();
                mealTypeDTO.setMeal(meal);
                mealTypeDTO.setType(typeRepo.findByTypeId(mealType.getId().getType_typeId()));
                mealTypeDTO.setUserOrderId(0);
                mealTypeDTOs.add(mealTypeDTO);
            }
        }
        return mealTypeDTOs;
    }

    private String viberXViberAuthTokenValue() {
        return environment.getProperty("viber.X-Viber-Auth-Token-Value");
    }

    private String viberMessageHeaderNormalOrder() {
        return environment.getProperty("viber.message.header.normalOrder");
    }

    private String viberMessageHeaderEearlyOrder() {
        return environment.getProperty("viber.message.header.earlyOrder");
    }

    private String viberMessageFooter() {
        return environment.getProperty("viber.message.footer");
    }

    private String viberMessageDateFormat() {
        return environment.getProperty("viber.message.dateFormat");
    }


}