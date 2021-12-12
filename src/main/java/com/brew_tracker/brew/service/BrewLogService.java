package com.brew_tracker.brew.service;

import com.brew_tracker.brew.model.BrewLogDto;

import java.util.List;

public interface BrewLogService {

    void createBrewLog(BrewLogDto brewLogDto);

    void updateBrewLog(BrewLogDto brewLogDto);

    void deleteBrewLog(int id);

    List<BrewLogDto> getAllBrewLogsForUser(String username);

    String getUsernameForLogById(int id);


}
