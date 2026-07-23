package com.smartpos.backend.dto;

import lombok.Data;

import java.util.List;

@Data
public class JwtResponse {
    private String token;
    private String type="Bearer";
    private String username;
    private List<String> roles;
    private List<String> lowStockAlerts;

    public JwtResponse(String accessToken, String username, List<String> roles, List<String> lowStockAlerts){
        this.token = accessToken;
        this.username = username;
        this.roles = roles;
        this.lowStockAlerts = lowStockAlerts;
    }
}
