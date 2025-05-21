package com.mycompany.flightapp.dto;

import lombok.Data;

import java.util.List;

@Data
public class PollDTO {
    private String question;
    private List<String> options;
    private boolean anonymous;
}
