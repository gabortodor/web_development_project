package com.brew_tracker.user.controller;

import com.brew_tracker.user.model.LoginData;
import com.brew_tracker.user.model.UserDto;
import com.brew_tracker.user.security.JwtTokenProvider;
import com.brew_tracker.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Optional;

@RestController
@RequestMapping("/user")
@CrossOrigin(
        //origins = "*",
        origins = {"http://localhost:3000"},
        allowCredentials = "true",
        maxAge = 3600,
        allowedHeaders = "*",
        methods= {RequestMethod.GET,RequestMethod.POST,
                RequestMethod.DELETE, RequestMethod.PUT,
                RequestMethod.PATCH, RequestMethod.OPTIONS,
                RequestMethod.HEAD, RequestMethod.TRACE}
)
public class UserController {


    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDto user) {
        try {
            return ResponseEntity.ok(userService.register(user));
        }
        catch (IllegalStateException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginData loginData, HttpServletResponse response) {
        try {
            String jwtToken = userService.login(loginData.getUsername(), loginData.getPassword());
            Cookie cookie = new Cookie("token", jwtToken);
            setupCookie(cookie, 3000);
            response.addCookie(cookie);
            return ResponseEntity.ok(jwtToken);
        }
        catch (AuthenticationException a) {
            return ResponseEntity.unprocessableEntity().body("Invalid credentials!");
        }
    }

    @DeleteMapping()
    public ResponseEntity<?> delete(HttpServletRequest request) {
        Optional<String> token = Arrays.stream(request.getCookies()).filter(cookie -> cookie.getName().equals("token")).map(Cookie::getValue).findFirst();
        if (token.isEmpty()) {
            throw new IllegalStateException("Cookie expired!");
        }
        userService.delete(jwtTokenProvider.getUsername(token.get()));
        return ResponseEntity.ok("User deleted");
    }

    @GetMapping("/info")
    public UserDto whoAmI(HttpServletRequest request) {
        Optional<String> token = Arrays.stream(request.getCookies()).filter(cookie -> cookie.getName().equals("token")).map(Cookie::getValue).findFirst();
        if (token.isEmpty()) {
            throw new IllegalStateException("Cookie expired!");
        }
        return userService.search(jwtTokenProvider.getUsername(token.get()));
    }

    @PostMapping("/logout")
    public String logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("token", null);
        setupCookie(cookie, 0);
        response.addCookie(cookie);
        userService.logout();
        return "Successful logout";
    }

    private void setupCookie(Cookie cookie, int age) {
        cookie.setMaxAge(age);
        cookie.setSecure(false);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
    }


}
