package com.brew_tracker.brew.service.impl;

import com.brew_tracker.brew.model.BrewLogDto;
import com.brew_tracker.brew.persistence.entity.BrewLog;
import com.brew_tracker.brew.persistence.repository.BrewLogRepository;
import com.brew_tracker.brew.service.BrewLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BrewLogServiceImpl implements BrewLogService {

    private final BrewLogRepository brewLogRepository;

    @Autowired
    public BrewLogServiceImpl(BrewLogRepository brewLogRepository) {
        this.brewLogRepository = brewLogRepository;
    }

    @Override
    public void createBrewLog(BrewLogDto brewLogDto) {
        brewLogRepository.save(convertDtoToEntity(brewLogDto));
    }

    @Override
    public void updateBrewLog(BrewLogDto brewLogDto) {
        brewLogRepository.save(convertDtoToEntity(brewLogDto));
    }

    @Override
    public void deleteBrewLog(BrewLogDto brewLogDto) {
        brewLogRepository.delete(convertDtoToEntity(brewLogDto));
    }

    @Override
    public List<BrewLogDto> getAllBrewLogsForUser(String username) {
        return brewLogRepository.findAllByUsername(username).stream().map(this::convertEntityToDto).collect(Collectors.toList());
    }

    BrewLog convertDtoToEntity(BrewLogDto brewLogDto){
        return BrewLog.builder()
                .id(brewLogDto.getId())
                .username(brewLogDto.getUsername())
                .brewDate(brewLogDto.getBrewDate())
                .batchSize(brewLogDto.getBatchSize())
                .type(brewLogDto.getType())
                .description(brewLogDto.getDescription())
                .build();
    }

    BrewLogDto convertEntityToDto(BrewLog brewLog) {
        return BrewLogDto.builder()
                .id(brewLog.getId())
                .username(brewLog.getUsername())
                .brewDate(brewLog.getBrewDate())
                .batchSize(brewLog.getBatchSize())
                .type(brewLog.getType())
                .description(brewLog.getDescription())
                .build();
    }

}
