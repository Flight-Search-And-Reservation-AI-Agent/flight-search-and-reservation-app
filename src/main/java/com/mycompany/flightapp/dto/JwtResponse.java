package com.mycompany.flightapp.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class JwtResponse {

    private String token;
    private Date expiryDate;

    public JwtResponse(String token, Date expiryDate) {
        this.token = token;
        this.expiryDate = expiryDate;
    }
}
