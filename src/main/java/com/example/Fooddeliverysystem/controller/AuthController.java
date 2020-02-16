package com.example.Fooddeliverysystem.controller;

import com.example.Fooddeliverysystem.dto.LoginDTO;
import com.example.Fooddeliverysystem.dto.UserDTO;
import com.example.Fooddeliverysystem.model.User;
import com.example.Fooddeliverysystem.security.TokenUtils;
import com.example.Fooddeliverysystem.service.UserServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.ValidationException;

@RestController
@RequestMapping("api")
public class AuthController {

    private AuthenticationManager authenticationManager;

    private UserDetailsService userDetailsService;

    private TokenUtils tokenUtils;

    private UserServiceI userService;

    @CrossOrigin
    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginDTO loginDTO) {
        try {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    loginDTO.getEmail(), loginDTO.getPassword());
            Authentication authentication = authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetails details = userDetailsService.loadUserByUsername(loginDTO.getEmail());
            UserDTO userDTO = userService.getUserByEmail(loginDTO.getEmail());

            String respBody = "{" +
                    "\"email\": \"" + details.getUsername() + "\"," +
                    "\"role\": \"" + userDTO.getRole() + "\"," +
                    "\"token\": \"" + tokenUtils.generateToken(details) + "\"" +
                    "}";

            return new ResponseEntity<>(respBody, HttpStatus.OK);
        } catch (ValidationException exp) {
            exp.printStackTrace();
            return new ResponseEntity<>("Invalid email addres or password", HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return new ResponseEntity<>("Invalid login", HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody User user) {
        User registeredUser = userService.registerUser(user);

        if (registeredUser == null) {
            return new ResponseEntity<>("This email already exists", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("You need to verify your account. Please check your email.", HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping("/resetPassword")
    public ResponseEntity<String> resetPassword(@RequestBody String email) {
        User user = userService.resetPassword(email);

        if (user == null) {
            return new ResponseEntity<>("This email don't exists!", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("You need to verify your account. Please check your email.", HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping("/confirmAccount")
    public ResponseEntity<String> confirmAccount(@RequestParam("token") String token) {
        User user = userService.confirmUserAccount(token);

        if (user == null)
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Autowired
    @Qualifier("userDetailsServiceImpl")
    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Autowired
    public void setUserService(UserServiceI userService) {
        this.userService = userService;
    }

    @Autowired
    public void setTokenUtils(TokenUtils tokenUtils) {
        this.tokenUtils = tokenUtils;
    }
}