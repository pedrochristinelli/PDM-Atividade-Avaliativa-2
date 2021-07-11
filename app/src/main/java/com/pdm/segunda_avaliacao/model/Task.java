package com.pdm.segunda_avaliacao.model;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

public class Task implements Serializable {
    private String createdBy;
    private String createdByUsername;
    private String endedBy;
    private String endedByUsername;
    private long createdAt;
    private long endedAt;
    private long endingForecastTime;
    private String title;
    private String description;
    private int status; // Status: 1- Novo; 2- Em progresso; 3- Finalizado

    public Task() {}

    public String getCreatedByUsername() {
        return createdByUsername;
    }

    public void setCreatedByUsername(String createdByUsername) {
        this.createdByUsername = createdByUsername;
    }

    public String getEndedByUsername() {
        return endedByUsername;
    }

    public void setEndedByUsername(String endedByUsername) {
        this.endedByUsername = endedByUsername;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getEndedBy() {
        return endedBy;
    }

    public void setEndedBy(String endedBy) {
        this.endedBy = endedBy;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getEndedAt() {
        return endedAt;
    }

    public void setEndedAt(long endedAt) {
        this.endedAt = endedAt;
    }

    public long getEndingForecastTime() {
        return endingForecastTime;
    }

    public void setEndingForecastTime(long endingForecastTime) {
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusString(){
        if (this.status == 1){
            return "Nova.";
        } else  if (this.status == 2){
            return "Em progresso.";
        } else if (this.status == 3){
            return "Finalizada.";
        } else{
            return "Status Desconhecido.";
        }
    }

    public String getEndingForecastTimeString(){
        Date date = new Date();
        date.setTime(this.endingForecastTime);
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        return dateFormat.format(date);
    }

    public String getEndedAtString(){
        Date date = new Date();
        date.setTime(this.endedAt);
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        return dateFormat.format(date);
    }

    public Map toMap(){
        ObjectMapper oMapper = new ObjectMapper();
        return oMapper.convertValue(this, Map.class);
    }
}
