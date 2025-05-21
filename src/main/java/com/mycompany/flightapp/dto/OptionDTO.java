package com.mycompany.flightapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OptionDTO {
    private String optionId; // Unique identifier for each option
    private String optionText;
    private int voteCount;
}
