package com.mycompany.flightapp.dto;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

@Data
public class ReservationDTO {

    @NotNull(message = "User ID cannot be null")
    private String userId;

    @NotNull(message = "Flight ID cannot be null")
    private String flightId;

    // Optional: if the client can select a seat
    private String seatNumber;

}
