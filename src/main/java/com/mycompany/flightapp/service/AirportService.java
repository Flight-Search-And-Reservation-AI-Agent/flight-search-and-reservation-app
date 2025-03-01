package com.mycompany.flightapp.service;

import com.mycompany.flightapp.model.Airport;

import java.util.List;
import java.util.Optional;

public interface AirportService {
    List<Airport> getAllAirports();

    Airport createAirport(Airport airport);

    Airport updateAirport(String airportId,Airport airport);

    Optional<Airport> getAirportById(String airportId);

    boolean deleteAirport(String airportId);

}
