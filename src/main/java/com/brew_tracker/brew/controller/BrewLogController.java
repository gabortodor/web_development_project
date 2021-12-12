package com.brew_tracker.brew.controller;

import com.brew_tracker.brew.model.BrewLogDto;
import com.brew_tracker.brew.model.UserToken;
import com.brew_tracker.brew.service.BrewLogService;
import com.brew_tracker.user.controller.UserController;
import com.brew_tracker.user.security.JwtTokenProvider;
import com.brew_tracker.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
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
public class BrewLogController {

    private final BrewLogService brewLogService;

    private JwtTokenProvider jwtTokenProvider;

    private UserService userService;

    @Autowired
    public BrewLogController(BrewLogService brewLogService, JwtTokenProvider jwtTokenProvider, UserService userService) {
        this.brewLogService = brewLogService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @PostMapping(path = "/create")
    public String createBrewLog(@RequestBody BrewLogDto brewLogDto, HttpServletRequest request){
        brewLogDto.setUsername(getUserNameFromCookie(request));
        brewLogService.createBrewLog(brewLogDto);
        return "Success!";
    }


    @PostMapping(path = "/update")
    public String updateBrewLog(@RequestBody BrewLogDto brewLogDto, HttpServletRequest request){
        if(brewLogDto.getUsername().equals(getUserNameFromCookie(request))) {
            brewLogService.updateBrewLog(brewLogDto);
            return "Success!";
        }
        else{
            return "Invalid user!";
        }
    }



    @PostMapping(path = "/delete")
    public String deleteBrewLog(@RequestBody String id, HttpServletRequest request){
        if(brewLogService.getUsernameForLogById(Integer.parseInt(id)).equals(getUserNameFromCookie(request))) {
            brewLogService.deleteBrewLog(Integer.parseInt(id));
            return "Success!";
        }
        else{
            return "Invalid user!";
        }
    }

    @PostMapping(path = "/get_logs")
    public ResponseEntity<?> getBrewLogs(@RequestBody UserToken userToken){
        List<BrewLogDto> brewLogs = brewLogService.getAllBrewLogsForUser(userToken.getUser());
        return ResponseEntity.ok(brewLogs);
    }

    private String getUserNameFromCookie(HttpServletRequest request){
        Optional<String> token = Arrays.stream(request.getCookies()).filter(cookie -> cookie.getName().equals("token")).map(Cookie::getValue).findFirst();
        if (token.isEmpty()) {
            throw new IllegalStateException("Cookie expired!");
        }
        return jwtTokenProvider.getUsername(token.get());
    }

}
