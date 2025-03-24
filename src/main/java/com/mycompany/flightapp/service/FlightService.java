package com.mycompany.flightapp.service;

import com.mycompany.flightapp.dto.FlightDTO;
import com.mycompany.flightapp.model.Flight;

import java.time.LocalDateTime;
import java.util.List;

public interface FlightService {
    Flight addFlight(FlightDTO flightDTO);

    Flight updateFlight(String id, FlightDTO flightDTO);

    boolean deleteFlight(String id);

    Flight getFlightById(String id);

    List<Flight> searchFlights(String originCode, String destinationCode, LocalDateTime departureDate);

}
