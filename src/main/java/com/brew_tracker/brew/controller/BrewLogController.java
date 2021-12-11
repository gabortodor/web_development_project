package com.brew_tracker.brew.controller;

import com.brew_tracker.brew.model.BrewLogDto;
import com.brew_tracker.brew.service.BrewLogService;
import com.brew_tracker.user.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api")
public class BrewLogController {

    private final BrewLogService brewLogService;

    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    public BrewLogController(BrewLogService brewLogService, JwtTokenProvider jwtTokenProvider) {
        this.brewLogService = brewLogService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping(path = "/create")
    public String createBrewLog(@RequestBody BrewLogDto brewLogDto){
        brewLogService.createBrewLog(brewLogDto);
        return "Success!";
    }


    @PostMapping(path = "/update")
    public String updateBrewLog(@RequestBody BrewLogDto brewLogDto){
        brewLogService.updateBrewLog(brewLogDto);
        return "Success!";
    }



    @PostMapping(path = "/delete")
    public String deleteBrewLog(@RequestBody BrewLogDto brewLogDto){
        brewLogService.deleteBrewLog(brewLogDto);
        return "Success!";
    }

    @PostMapping(path = "/get_logs")
    public ResponseEntity<?> getBrewLogs(HttpServletRequest req){
        List<BrewLogDto> brewLogs = brewLogService.getAllBrewLogsForUser(jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(req)));
        return ResponseEntity.ok(brewLogs);
    }
}
