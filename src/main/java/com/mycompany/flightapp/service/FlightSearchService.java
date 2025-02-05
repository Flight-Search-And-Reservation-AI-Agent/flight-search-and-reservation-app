package com.mycompany.flightapp.service;

import com.mycompany.flightapp.model.Flight;
import com.mycompany.flightapp.repository.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FlightSearchService {

    private final FlightRepository flightRepository;

    @Autowired
    public FlightSearchService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    public List<Flight> searchFlights(String originCode, String destinationCode, LocalDateTime departureDate) {
        // Define the start and end of the day for the given departureDate.
        LocalDateTime start = departureDate.withHour(0).withMinute(0).withSecond(0);
        LocalDateTime end = departureDate.withHour(23).withMinute(59).withSecond(59);
        return flightRepository.findByOrigin_CodeAndDestination_CodeAndDepartureTimeBetween(
                originCode, destinationCode, start, end);
    }

}
