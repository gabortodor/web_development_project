package com.brew_tracker.brew.persistence.repository;

import com.brew_tracker.brew.persistence.entity.BrewLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrewLogRepository extends JpaRepository<BrewLog, Integer> {
    List<BrewLog> findAllByUsername(String username);
}
