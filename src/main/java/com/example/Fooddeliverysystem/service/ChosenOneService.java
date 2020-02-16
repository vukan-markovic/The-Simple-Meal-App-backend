package com.example.Fooddeliverysystem.service;

import com.example.Fooddeliverysystem.dto.MealTypeDTO;
import com.example.Fooddeliverysystem.dto.UserDTO;
import com.example.Fooddeliverysystem.model.Role;
import com.example.Fooddeliverysystem.model.User;
import com.example.Fooddeliverysystem.model.Userorder;
import com.example.Fooddeliverysystem.repository.RoleRepo;
import com.example.Fooddeliverysystem.repository.UserRepo;
import com.example.Fooddeliverysystem.repository.UserorderRepo;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
@Transactional
public class ChosenOneService implements ChosenOneServiceI {

    private static final String URL= "https://fcm.googleapis.com/fcm/send";

    private UserRepo userRepo;

    private RoleRepo roleRepo;

    private UserorderRepo userorderRepo;

    private Environment environment;


    @Override
    public void payingNotification(int[] userIds) {
        if (userIds.length == 0) {
            throw new IllegalArgumentException();
        }

        User chosenOne = userRepo.findByRole(roleRepo.findByNameIgnoreCase("CHOSEN"));
        if (chosenOne == null) {
            throw new IllegalStateException();
        }

        for (int userId : userIds) {
            User user = userRepo.findByUserId(userId);

            this.sendNotification(user.getToken(), this.chosenOneMessagePayingNotificationTitle(),
                    chosenOneMessagePayingNotificationBody() + chosenOne.getName() + " " + chosenOne.getLastName() ,
                    this.chosenOneMessagePayingNotificationClickAction());
        }
    }


    @Scheduled(cron = "${chosenOne.timer.notifyChosenOne}")
    public void notifyChosenOne() {
        this.selectChosenOne();
        User chosenOne = userRepo.findByRole(roleRepo.findByNameIgnoreCase("CHOSEN"));
        if (chosenOne == null)
            return;
        this.sendNotification(chosenOne.getToken(), this.chosenOneMessageNotifyChosenOneTitle(),
                this.chosenOneMessageNotifyChosenOneBody(), this.chosenOneMessageNotifyChosenOneClickAction());
    }

    @Scheduled(cron = "${chosenOne.timer.eliminateChosenOne}")
    public void eliminateChosenOne() {
        Role role = roleRepo.findByNameIgnoreCase("CHOSEN");
        User chosenOne = userRepo.findByRole(role);
        if (chosenOne != null) {
            role = roleRepo.findByNameIgnoreCase("USER");
            chosenOne.setRole(role);
            role.getUsers().add(chosenOne);

            userRepo.save(chosenOne);
            roleRepo.save(role);

            this.sendNotification(chosenOne.getToken(), this.chosenOneMessageEliminateChosenOneTitle(),
                    this.chosenOneMessageEliminateChosenOneBody(), this.chosenOneMessageEliminateChosenOneClickAction());

        }
    }

    private void sendNotification(String token, String title, String body, String clickAction) {
        try {
            URL url = new URL(URL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setRequestProperty("Authorization", this.notificationAuthorizationKey());
            con.setDoOutput(true);

            JSONObject foodNotification = new JSONObject();
            JSONObject notification = new JSONObject();

            notification.put("title",title);
            notification.put("body",body);

            notification.put("click_action", clickAction);

            foodNotification.put("notification",notification);
            foodNotification.put("to",token);

            sendRequest(con, foodNotification.toJSONString());
        } catch (IOException exp) {
            System.out.println(exp.getMessage());
        }
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
            System.out.println(response.toString());
        }
    }

    private boolean haveUserWithTodayOrders(List<User> users) {
        for (User u : users) {
            if (u.getRole().getName().equals("USER")
                    && !userorderRepo.findByUserUserIdAndDate(u.getUserId(), new Date()).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void selectChosenOne() {
        List<User> users = userRepo.findAll();
        Random random = new Random();
        User randomUser = users.get(random.nextInt(users.size()));
        while (randomUser.getRole().getName().equals("ADMIN")
                || userorderRepo.findByUserUserIdAndDate(randomUser.getUserId(), new Date()).isEmpty()) {
            if (!this.haveUserWithTodayOrders(users))
                return;
            users = userRepo.findAll();
            randomUser = users.get(random.nextInt(users.size()));
        }

        Role role = roleRepo.findByNameIgnoreCase("CHOSEN");
        if (role == null) {
            return;
        }
        role.getUsers().add(randomUser);
        randomUser.setRole(role);

        roleRepo.save(role);
        userRepo.save(randomUser);
    }

    @Override
    public void setPaid(MealTypeDTO mealTypeDTO) {
        Userorder userorder = userorderRepo.findById(mealTypeDTO.getUserOrderId()).get();
        userorder.setPaid(mealTypeDTO.isPaid());
        userorderRepo.save(userorder);
    }



    public UserDTO getChosenOne() {
        User chosenOne = userRepo.findByRole(roleRepo.findByNameIgnoreCase("CHOSEN"));
        if (chosenOne != null) {
            UserDTO userDTO = new UserDTO();
            userDTO.setUserId(chosenOne.getUserId());
            userDTO.setRole(chosenOne.getRole().getName());
            userDTO.setPathImage(chosenOne.getPathImage());
            userDTO.setName(chosenOne.getName());
            userDTO.setLastName(chosenOne.getLastName());
            userDTO.setEmail(chosenOne.getEmail());

            return userDTO;
        }
        return null;
    }




    @Autowired
    public void setUserRepo(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Autowired
    public void setRoleRepo(RoleRepo roleRepo) {
        this.roleRepo = roleRepo;
    }

    @Autowired
    public void setUserOrderRepo(UserorderRepo userorderRepo) {
        this.userorderRepo = userorderRepo;
    }

    @Autowired
    public void setEnvironment(Environment environment) { this.environment = environment; }

    private String notificationAuthorizationKey() {
        return environment.getProperty("notification.authorization.key");
    }

    private String chosenOneMessageNotifyChosenOneTitle() {
        return environment.getProperty("chosenOne.message.notifyChosenOne.title");
    }

    private String chosenOneMessageNotifyChosenOneBody() {
        return environment.getProperty("chosenOne.message.notifyChosenOne.body");
    }

    private String chosenOneMessageNotifyChosenOneClickAction() {
        String url = environment.getProperty("url.frontend")+environment.getProperty("chosenOne.message.notifyChosenOne.clickAction");
        return url;
    }

    private String chosenOneMessageEliminateChosenOneTitle() {
        return environment.getProperty("chosenOne.message.eliminateChosenOne.title");
    }
    private String chosenOneMessageEliminateChosenOneBody() {
        return environment.getProperty("chosenOne.message.eliminateChosenOne.body");
    }
    private String chosenOneMessageEliminateChosenOneClickAction() {
        return environment.getProperty("url.frontend");
    }

    private String chosenOneMessagePayingNotificationTitle(){
        return environment.getProperty("chosenOne.message.payingNotification.title");
    }
    private String chosenOneMessagePayingNotificationBody(){
        return environment.getProperty("chosenOne.message.payingNotification.body");
    }
    private String chosenOneMessagePayingNotificationClickAction(){
        return environment.getProperty("url.frontend");
    }




}