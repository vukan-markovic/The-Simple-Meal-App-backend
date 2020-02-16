package com.example.Fooddeliverysystem.service;

import com.example.Fooddeliverysystem.dto.UserDTO;
import com.example.Fooddeliverysystem.model.Confirmationtoken;
import com.example.Fooddeliverysystem.model.Role;
import com.example.Fooddeliverysystem.model.User;
import com.example.Fooddeliverysystem.repository.ConfirmationTokenRepo;
import com.example.Fooddeliverysystem.repository.RoleRepo;
import com.example.Fooddeliverysystem.repository.UserRepo;
import com.example.Fooddeliverysystem.enums.Status;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

@Service
public class UserServiceImpl implements UserServiceI {



    private UserRepo userRepo;
    private RoleRepo roleRepo;
    private ConfirmationTokenRepo confirmationTokenRepo;
    private EmailSenderServiceI emailSenderService;
    private Environment environment;

    @Override
    public User registerUser(User user) {

        User existingUser = userRepo.findByEmail(user.getEmail());
        if (existingUser != null) {
            return null;
        }
        user.setStatus(Status.INACTIVE.getValue());

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));

        Role userRole = roleRepo.findByNameIgnoreCase("USER");
        user.setRole(userRole);
        User newUser = userRepo.save(user);
        sendConfirmationMail(user);
        return newUser;
    }

    @Override
    public User resetPassword(String email) {
        User user = userRepo.findByEmail(email);
        if (user == null) return null;
        else {
            sendResetPasswordMail(user);
            return user;
        }
    }

    private void sendConfirmationMail(User user) {
        Confirmationtoken confirmationToken = createConfirmationtoken(user);


        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject(this.sendConfirmationMailTitle());

        StringBuilder messageBody = new StringBuilder();
        messageBody.append(this.sendConfirmationMailBody());
        messageBody.append(this.loginPage() + "?token=" + confirmationToken.getConfirmationToken());
        messageBody.append(this.mailMessageFooter());

        mailMessage.setText(messageBody.toString());

        emailSenderService.sendEmail(mailMessage);
    }


    private Confirmationtoken createConfirmationtoken(User user){
        Confirmationtoken confirmationToken = new Confirmationtoken();
        confirmationToken.setUser(user);
        confirmationToken.setCreatedDate(new Date());
        confirmationToken.setConfirmationToken(generateToken());

        return confirmationTokenRepo.save(confirmationToken);
    }
    private void sendResetPasswordMail(User user) {
        Confirmationtoken confirmationToken = createConfirmationtoken(user);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject(this.sendResetPasswordMailTitle());

        StringBuilder messageBody = new StringBuilder();
        messageBody.append(this.sendResetPasswordMailBody());
        messageBody.append(this.resetPasswordPage()+ "?token=" + confirmationToken.getConfirmationToken());
        messageBody.append(this.mailMessageFooter());

        mailMessage.setText(messageBody.toString());

        emailSenderService.sendEmail(mailMessage);
    }

    private String generateToken() {
        int n = 30;
        byte[] array = new byte[256];
        new Random().nextBytes(array);

        String randomString = new String(array, StandardCharsets.UTF_8);

        StringBuilder r = new StringBuilder();

        String AlphaNumericString = randomString.replaceAll("[^A-Za-z0-9]", "");

        for (int k = 0; k < AlphaNumericString.length(); k++) {
            if (Character.isLetter(AlphaNumericString.charAt(k)) && (n > 0) || Character.isDigit(AlphaNumericString.charAt(k)) && (n > 0)) {
                r.append(AlphaNumericString.charAt(k));
                n--;
            }
        }
        return r.toString();
    }


    @Override
    public User confirmUserAccount(String token) {
        Confirmationtoken confirmationtoken = confirmationTokenRepo.findByConfirmationToken(token);
        if (confirmationtoken != null) {
            User user = userRepo.findByUserIdAndStatus(confirmationtoken.getUser().getUserId(), Status.INACTIVE.getValue());
            user.setStatus(Status.ACTIVE.getValue());
            userRepo.save(user);
            confirmationtoken.setConfirmedDate(new Date());
            confirmationTokenRepo.save(confirmationtoken);
            return user;
        }
        return null;
    }

    @Override
    public UserDTO getUser(int userId) {
        User user = userRepo.findByUserIdAndStatus(userId, Status.ACTIVE.getValue());
        if (user == null)
            return null;
        return UserDTO.mapper(user);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepo.findAll();
        List<UserDTO> result = new LinkedList<>();
        for (User user : users)
            if (user.getStatus() != Status.DELETED.getValue() && user.getStatus() != Status.INACTIVE.getValue())
                result.add(UserDTO.mapper(user));

        return result;
    }


    @Override
    public boolean updateUser(int userId, String command) {
        User user = userRepo.findByUserId(userId);
        if (user == null)

            System.out.println("Upadate user! userId= " + userId + " command= " + command);
        if (user == null)

            return false;
        switch (command.toUpperCase()) {
            case "ACTIVE":
                user.setStatus(Status.ACTIVE.getValue());
                userRepo.save(user);
                break;

            case "BAN":
                user.setStatus(Status.BANNED.getValue());
                userRepo.save(user);
                break;

            case "DELETE":
                user.setStatus(Status.DELETED.getValue());
                userRepo.save(user);
                break;

            case "PROMOTE":
                Role adminRole = roleRepo.findByNameIgnoreCase("ADMIN");
                user.setRole(adminRole);
                userRepo.save(user);
                break;

            case "DEMOTION":
                Role userRole = roleRepo.findByNameIgnoreCase("USER");
                user.setRole(userRole);
                userRepo.save(user);
                break;

            default:
                return false;
        }
        return true;
    }

    @Override
    public ResponseEntity<String> updateUserEmail(Integer id, String email) {
        if (id == null || email == null) return new ResponseEntity<>("Id and email must not be null!", HttpStatus.BAD_REQUEST);
        if (!userRepo.existsById(id)) return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        if (userRepo.findByEmail(email) != null)
            return new ResponseEntity<>("This email already exists", HttpStatus.BAD_REQUEST);
        User user = userRepo.findByUserId(id);
        user.setEmail(email);
        userRepo.save(user);
        sendConfirmationMail(user);
        return new ResponseEntity<>("You need to verify your account again. Please check your email.", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> updateUserImage(Integer id, String imagePath) {
        if (id == null || imagePath == null) return new ResponseEntity<>("Id and image path must not be null!", HttpStatus.BAD_REQUEST);
        if (!userRepo.existsById(id)) return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        User user = userRepo.findByUserId(id);
        user.setPathImage(new String(Base64.encodeBase64(imagePath.getBytes())));
        userRepo.save(user);
        return new ResponseEntity<>("Image update successfully", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> updateUserPassword(String token, String password) {
        if (token == null || password == null) return new ResponseEntity<>("Token and password must not be null!", HttpStatus.BAD_REQUEST);
        Confirmationtoken confirmationtoken = confirmationTokenRepo.findByConfirmationToken(token);
        User user = userRepo.findByUserId(confirmationtoken.getUser().getUserId());
        if (user == null) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(password));
        userRepo.save(user);
        return new ResponseEntity<>("Password update successfully", HttpStatus.OK);
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        User user = userRepo.findByEmailAndStatus(email, Status.ACTIVE.getValue());
        if (user != null) {
            if (user.getPathImage() != null)
                user.setPathImage(new String(Base64.decodeBase64(user.getPathImage())));
            return UserDTO.mapper(user);
        }
        return null;
    }



    private String sendConfirmationMailTitle() {
        return environment.getProperty("authentication.mailMessage.sendConfirmationMail.title");
    }

    private String sendConfirmationMailBody() {
        return environment.getProperty("authentication.mailMessage.sendConfirmationMail.body");
    }

    private String mailMessageFooter() {
        return environment.getProperty("authentication.mailMessage.footer");
    }

    private String loginPage(){
        return environment.getProperty("url.frontend") + environment.getProperty("authentication.url.login");
    }

    private String resetPasswordPage(){
        return environment.getProperty("url.frontend") + environment.getProperty("resetPassword");
    }

    private String sendResetPasswordMailTitle() {
        return environment.getProperty("authentication.mailMessage.sendResetPasswordMail.title");
    }

    private String sendResetPasswordMailBody() {
        return environment.getProperty("authentication.mailMessage.sendResetPasswordMail.body");
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
    public void setConfirmationTokenRepo(ConfirmationTokenRepo confirmationTokenRepo) {
        this.confirmationTokenRepo = confirmationTokenRepo;
    }

    @Autowired
    public void setEmailSenderService(EmailSenderServiceI emailSenderService) {
        this.emailSenderService = emailSenderService;
    }
    @Autowired
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
