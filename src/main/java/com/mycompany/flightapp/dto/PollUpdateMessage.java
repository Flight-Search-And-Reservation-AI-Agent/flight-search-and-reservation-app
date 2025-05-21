package com.mycompany.flightapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PollUpdateMessage {
    private String pollId;
    private List<OptionDTO> options;
}
