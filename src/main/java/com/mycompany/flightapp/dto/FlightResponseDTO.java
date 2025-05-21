package com.mycompany.flightapp.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FlightResponseDTO {

    @NotNull(message = "Flight ID is required")
    private String flightId;

    @NotNull(message = "Flight number is required")
    private String flightNumber;

    @NotNull(message = "Departure time is required")
    private LocalDateTime departureTime;

    @NotNull(message = "Arrival time is required")
    private LocalDateTime arrivalTime;

    @NotNull(message = "Origin airport ID is required")
    private String originAirportId;

    @NotNull(message = "Destination airport name is required")
    private String originAirportName;

    @NotNull(message = "Destination airport ID is required")
    private String destinationAirportId;

    @NotNull(message = "Destination airport name is required")
    private String destinationAirportName;

    @NotNull(message = "Aircraft ID is required")
    private String aircraftId;

    @NotNull
    private String airline;

    @NotNull(message = "Price is required")
    private Double price;


}
