package com.pdm.segunda_avaliacao.model;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.Map;

public class Task {
    private int createdBy;
    private int endedBy;
    private LocalDateTime createdAt;
    private LocalDateTime endedAt;
    private LocalDateTime endingForecastTime;
    private String title;
    private String description;

    public Task() {}

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public int getEndedBy() {
        return endedBy;
    }

    public void setEndedBy(int endedBy) {
        this.endedBy = endedBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getEndedAt() {
        return endedAt;
    }

    public void setEndedAt(LocalDateTime endedAt) {
        this.endedAt = endedAt;
    }

    public LocalDateTime getEndingForecastTime() {
        return endingForecastTime;
    }

    public void setEndingForecastTime(LocalDateTime endingForecastTime) {
        this.endingForecastTime = endingForecastTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public Map toMap(){
        ObjectMapper oMapper = new ObjectMapper();
        return oMapper.convertValue(this, Map.class);
    }
}
