package com.mycompany.flightapp.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Data
public class CreateReservationRequest {
    @NotNull(message = "User ID cannot be null")
    private String userId;
    @NotNull(message = "Flight ID cannot be null")
    private String flightId;
    private List<PassengerDTO> passengers;
}
