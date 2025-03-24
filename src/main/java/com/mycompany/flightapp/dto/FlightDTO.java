package com.mycompany.flightapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlightDTO {

    @NotNull(message = "Flight number is required")
    private String flightNumber;

    @NotNull(message = "Departure time is required")
    private LocalDateTime departureTime;

    @NotNull(message = "Arrival time is required")
    private LocalDateTime arrivalTime;

    @NotNull(message = "Origin airport ID is required")
    private String originAirportId;

    @NotNull(message = "Destination airport ID is required")
    private String destinationAirportId;

    @NotNull(message = "Aircraft ID is required")
    private String aircraftId;

    @NotNull(message = "Price is required")
    private Double price;


}
