package com.mycompany.flightapp.service;

import com.mycompany.flightapp.dto.FlightDTO;
import com.mycompany.flightapp.model.Flight;

public interface FlightService {
    Flight addFlight(FlightDTO flightDTO);

    Flight updateFlight(String id, FlightDTO flightDTO);

    boolean deleteFlight(String id);

    Flight getFlightById(String id);
}
