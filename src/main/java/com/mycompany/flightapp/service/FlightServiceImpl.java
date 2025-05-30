package com.mycompany.flightapp.service;

import com.mycompany.flightapp.dto.FlightDTO;
import com.mycompany.flightapp.dto.FlightResponseDTO;
import com.mycompany.flightapp.exception.ResourceNotFoundException;
import com.mycompany.flightapp.model.Aircraft;
import com.mycompany.flightapp.model.Airport;
import com.mycompany.flightapp.model.Flight;
import com.mycompany.flightapp.repository.AircraftRepository;
import com.mycompany.flightapp.repository.AirportRepository;
import com.mycompany.flightapp.repository.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public List<FlightResponseDTO> getAllFlights() {
        List<Flight> flights = flightRepository.findAll();
        return flights.stream().map(flight -> {
            return FlightResponseDTO.builder()
                    .flightId(flight.getFlightId())
                    .flightNumber(flight.getFlightNumber())
                    .departureTime(flight.getDepartureTime())
                    .arrivalTime(flight.getArrivalTime())
                    .originAirportId(flight.getOrigin().getAirportId())
                    .originAirportName(flight.getOrigin().getName())
                    .destinationAirportId(flight.getDestination().getAirportId())
                    .destinationAirportName(flight.getDestination().getName())
                    .aircraftId(flight.getAircraft().getAircraftId()) // Assuming same structure for Aircraft
                    .airline(flight.getAircraft().getAirline())
                    .price(flight.getPrice())
                    .build();
        }).collect(Collectors.toList());

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

    @Override
    public List<Flight> searchFlights(String originCode, String destinationCode, LocalDateTime departureDate) {
        // Define the start and end of the day for the given departureDate.
        LocalDateTime start = departureDate.withHour(0).withMinute(0).withSecond(0);
        LocalDateTime end = departureDate.withHour(23).withMinute(59).withSecond(59);
        return flightRepository.findByOrigin_CodeAndDestination_CodeAndDepartureTimeBetween(
                originCode, destinationCode, start, end);
    }

}
