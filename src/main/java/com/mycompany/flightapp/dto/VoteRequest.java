package com.mycompany.flightapp.dto;

import lombok.Data;

@Data
public class VoteRequest {
    private String pollId;
    private String optionId;
}
