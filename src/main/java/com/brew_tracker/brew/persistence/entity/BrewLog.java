package com.brew_tracker.brew.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BrewLog {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    private String username;

    private String brewDate;

    private int batchSize;

    private String type;

    private String description;

    public BrewLog(String brewDate, int batchSize, String type, String description) {
        this.brewDate = brewDate;
        this.batchSize = batchSize;
        this.type = type;
        this.description = description;
    }
}
