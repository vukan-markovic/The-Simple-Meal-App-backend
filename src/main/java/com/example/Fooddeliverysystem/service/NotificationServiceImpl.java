package com.example.Fooddeliverysystem.service;

import com.example.Fooddeliverysystem.model.User;
import com.example.Fooddeliverysystem.repository.UserRepo;
import com.example.Fooddeliverysystem.repository.UserorderRepo;
import com.example.Fooddeliverysystem.security.TokenUtils;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class NotificationServiceImpl implements NotificationServiceI {

    private static final String URL= "https://fcm.googleapis.com/fcm/send";

    private UserRepo userRepo;

    private TokenUtils tokenUtils;

    private UserorderRepo userOrderRepo;

    private List<User> notifiedUsers;

    private Environment environment;


    @Scheduled(cron = "${notification.orderFoodNotification.timer}")
    public void orderFoodNotification() {
        List<String> tokenList = getNotifiedUsers(true);
        sendNotification(this.notificationOrderFoodNotificationTitle(),this.notificationOrderFoodNotificationBody(),tokenList);
    }


    @Scheduled(cron = "${notification.orderFoodLastNotification.timer}")
    public void orderFoodLastNotification() {
        List<String> tokenList = getNotifiedUsers(true);
        sendNotification(this.notificationOrderFoodLastNotificationTitle(),this.notificationOrderFoodLastNotificationBody(),tokenList);
    }


    @Scheduled(cron = "${notification.orderFoodLastNotificationForTomorrow.timer}")
    public void orderFoodLastNotificationForTomorrow() {
        List<String> tokenList = getNotifiedUsers(false);
        sendNotification(this.notificationOrderFoodLastNotificationForTomorrowTitle(),this.notificationOrderFoodLastNotificationForTomorrowBody(),tokenList);
    }


    private void sendNotification(String title, String body, List<String> tokenLIst) {
        try {
            URL url = new URL(URL);
            for (String token : tokenLIst) {
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json; utf-8");
                con.setRequestProperty("Accept", "application/json");
                con.setRequestProperty("Authorization", notificationAuthorizationKey());
                con.setDoOutput(true);

                JSONObject foodNotification = new JSONObject();
                JSONObject notification = new JSONObject();

                notification.put("title",title);
                notification.put("body",body);

                notification.put("click_action",this.frontendUrl());

                foodNotification.put("notification",notification);
                foodNotification.put("to",token);

                sendRequest(con, foodNotification.toJSONString());
            }
        }catch (IOException  e){
            e.printStackTrace();
        }
    }

    private List<String> getNotifiedUsers(boolean today) {
        Date date;

        if(today){
            date = new Date();
        }else{
            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            c.add(Calendar.DATE, 1);
            date = c.getTime();
        }

        List<User> users = userRepo.findAll();
        List<String> tokenList = new ArrayList<>();
        for (User user : users) {
            if (userOrderRepo.findByUserUserIdAndDate(user.getUserId(), date).isEmpty())
                if(user.getToken()!=null){
                    tokenList.add(user.getToken());
                }
        }
        return tokenList;
    }

    private void sendRequest(HttpURLConnection con, String jsonInputString) throws IOException {
        try (OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        } catch (IOException exp) {
            System.out.println(exp.getMessage());
        }

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
        }
    }

    @Override
    public boolean saveToken(String token, User user) {
        if (token.isEmpty()) {
            return false;
        }
        user.setToken(token);
        userRepo.save(user);
        return true;
    }

    @Override
    public User getUser(String token) {
        if (token.isEmpty()) {
            return null;
        }
        return userRepo.findByEmail(tokenUtils.getUsernameFromToken(token));
    }
    private String frontendUrl() {
        return environment.getProperty("url.frontend ");
    }

    private String notificationOrderFoodNotificationTitle() {
        return environment.getProperty("notification.orderFoodNotification.title");
    }

    private String notificationOrderFoodNotificationBody() {
        return environment.getProperty("notification.orderFoodNotification.body");
    }

    private String notificationOrderFoodLastNotificationTitle() {
        return environment.getProperty("notification.orderFoodLastNotification.title");
    }

    private String notificationOrderFoodLastNotificationBody() {
        return environment.getProperty("notification.orderFoodLastNotification.body");
    }

    private String notificationOrderFoodLastNotificationForTomorrowTitle() {
        return environment.getProperty("notification.orderFoodLastNotificationForTomorrow.title");
    }

    private String notificationOrderFoodLastNotificationForTomorrowBody() {
        return environment.getProperty("notification.orderFoodLastNotificationForTomorrow.body");
    }

    private String notificationAuthorizationKey() {
        return environment.getProperty("notification.authorization.key");
    }

    @Autowired
    public void setTokenUtils(TokenUtils tokenUtils) {
        this.tokenUtils = tokenUtils;
    }

    @Autowired
    public void setUserRepo(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Autowired
    public void setUserOrderRepo(UserorderRepo userOrderRepo) {
        this.userOrderRepo = userOrderRepo;
    }

    @Autowired
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
