package com.mycompany.flightapp.service;

import com.mycompany.flightapp.dto.FlightDTO;
import com.mycompany.flightapp.model.Flight;

public interface FlightService {
    Flight addFlight(FlightDTO flightDTO);
    // Additional flight-related methods can be added here
}
