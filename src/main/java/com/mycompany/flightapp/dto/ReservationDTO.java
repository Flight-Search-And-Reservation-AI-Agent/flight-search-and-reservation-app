package com.mycompany.flightapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDTO {

    @NotNull(message = "User ID cannot be null")
    private String userId;

    @NotNull(message = "Flight ID cannot be null")
    private String flightId;

    // Optional: if the client can select a seat
    private String seatNumber;

}
