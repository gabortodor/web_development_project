package com.brew_tracker.user.controller;

import com.brew_tracker.user.model.LoginData;
import com.brew_tracker.user.model.UserDto;
import com.brew_tracker.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
public class UserController {


    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDto user) {
        try {
            return ResponseEntity.ok(userService.register(user));
        }
        catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginData loginData) {
        try {
            String jwtToken = userService.login(loginData.getUsername(), loginData.getPassword());
            return ResponseEntity.ok(jwtToken);
        }
        catch (AuthenticationException a) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid credentials");
        }
    }

    @DeleteMapping()
    public ResponseEntity<?> delete(HttpServletRequest re) {
        userService.delete(re);
        return ResponseEntity.ok("User deleted");
    }




}
