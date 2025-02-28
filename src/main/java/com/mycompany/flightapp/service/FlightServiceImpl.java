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

import java.util.Optional;

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

    @Override
    public Flight updateFlight(String id, FlightDTO flightDTO) {
        Flight existingFlight = flightRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found with id: " + id));

        // Update basic flight details
        existingFlight.setFlightNumber(flightDTO.getFlightNumber());
        existingFlight.setDepartureTime(flightDTO.getDepartureTime());
        existingFlight.setArrivalTime(flightDTO.getArrivalTime());
        existingFlight.setPrice(flightDTO.getPrice());

        // Update origin airport if needed
        if (flightDTO.getOriginAirportId() != null) {
            Airport origin = airportRepository.findById(flightDTO.getOriginAirportId())
                    .orElseThrow(() -> new ResourceNotFoundException("Origin Airport not found with id: " + flightDTO.getOriginAirportId()));
            existingFlight.setOrigin(origin);
        }

        // Update destination airport if needed
        if (flightDTO.getDestinationAirportId() != null) {
            Airport destination = airportRepository.findById(flightDTO.getDestinationAirportId())
                    .orElseThrow(() -> new ResourceNotFoundException("Destination Airport not found with id: " + flightDTO.getDestinationAirportId()));
            existingFlight.setDestination(destination);
        }

        // Update aircraft if needed
        if (flightDTO.getAircraftId() != null) {
            Aircraft aircraft = aircraftRepository.findById(flightDTO.getAircraftId())
                    .orElseThrow(() -> new ResourceNotFoundException("Aircraft not found with id: " + flightDTO.getAircraftId()));
            existingFlight.setAircraft(aircraft);
        }

        return flightRepository.save(existingFlight);
    }

    @Override
    public boolean deleteFlight(String id){
        Optional<Flight> optionalDelete = flightRepository.findById(id);
        if(!optionalDelete.isPresent())return false;
        Flight flight=optionalDelete.get();
        flightRepository.delete(flight);
        return true;
    }

    @Override
    public  Flight getFlightById(String id){
        Optional<Flight> optionalFlight = flightRepository.findById(id);
        return optionalFlight.orElse(null);
    }
}
