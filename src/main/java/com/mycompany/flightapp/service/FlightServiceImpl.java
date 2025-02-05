package com.mycompany.flightapp.service;

import com.mycompany.flightapp.dto.FlightDTO;
import com.mycompany.flightapp.exception.ResourceNotFoundException;
import com.mycompany.flightapp.model.Aircraft;
import com.mycompany.flightapp.model.Airport;
import com.mycompany.flightapp.model.Flight;
import com.mycompany.flightapp.repository.AircraftRepository;
import com.mycompany.flightapp.repository.AirportRepository;
import com.mycompany.flightapp.repository.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FlightServiceImpl implements FlightService {

    private final FlightRepository flightRepository;
    private final AirportRepository airportRepository;
    private final AircraftRepository aircraftRepository;

    @Autowired
    public FlightServiceImpl(FlightRepository flightRepository,
                             AirportRepository airportRepository,
                             AircraftRepository aircraftRepository) {
        this.flightRepository = flightRepository;
        this.airportRepository = airportRepository;
        this.aircraftRepository = aircraftRepository;
    }

    @Override
    public Flight addFlight(FlightDTO flightDTO) {
        // Fetch the origin airport
        Airport origin = airportRepository.findById(flightDTO.getOriginAirportId())
                .orElseThrow(() -> new ResourceNotFoundException("Origin Airport not found"));

        // Fetch the destination airport
        Airport destination = airportRepository.findById(flightDTO.getDestinationAirportId())
                .orElseThrow(() -> new ResourceNotFoundException("Destination Airport not found"));

        // Fetch the aircraft
        Aircraft aircraft = aircraftRepository.findById(flightDTO.getAircraftId())
                .orElseThrow(() -> new ResourceNotFoundException("Aircraft not found"));

        // Create and save the Flight entity
        Flight flight = new Flight();
        flight.setFlightNumber(flightDTO.getFlightNumber());
        flight.setDepartureTime(flightDTO.getDepartureTime());
        flight.setArrivalTime(flightDTO.getArrivalTime());
        flight.setOrigin(origin);
        flight.setDestination(destination);
        flight.setAircraft(aircraft);
        flight.setPrice(flightDTO.getPrice());

        return flightRepository.save(flight);
    }
}
