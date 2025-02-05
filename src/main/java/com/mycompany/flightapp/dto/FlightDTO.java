package com.mycompany.flightapp.dto;

import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class FlightDTO {

    @NotNull(message = "Flight number is required")
    private String flightNumber;

    @NotNull(message = "Departure time is required")
    private LocalDateTime departureTime;

    @NotNull(message = "Arrival time is required")
    private LocalDateTime arrivalTime;

    @NotNull(message = "Origin airport ID is required")
    private Long originAirportId;

    @NotNull(message = "Destination airport ID is required")
    private Long destinationAirportId;

    @NotNull(message = "Aircraft ID is required")
    private Long aircraftId;

    @NotNull(message = "Price is required")
    private Double price;


}
