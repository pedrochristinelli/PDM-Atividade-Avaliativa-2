package com.pdm.segunda_avaliacao.model;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class LocalUser {
    private String id;
    private String username;
    private String email;
    private String password;

    public LocalUser() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Map toMap(){
        ObjectMapper oMapper = new ObjectMapper();
        return oMapper.convertValue(this, Map.class);
    }
}
