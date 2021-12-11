package com.brew_tracker.brew.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class BrewLogDto {
    private int id;

    private String username;

    private String brewDate;

    private int batchSize;

    private String type;

    private String description;
}
