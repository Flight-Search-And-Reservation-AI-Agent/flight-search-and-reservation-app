package com.mycompany.flightapp.controller;

import com.mycompany.flightapp.dto.FlightDTO;
import com.mycompany.flightapp.model.Flight;
import com.mycompany.flightapp.service.FlightSearchService;
import com.mycompany.flightapp.service.FlightService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/flights")
public class FlightController {

    private final FlightSearchService flightSearchService;
    private final FlightService flightService;

    @Autowired
    public FlightController(FlightSearchService flightSearchService, FlightService flightService) {
        this.flightSearchService = flightSearchService;
        this.flightService = flightService;
    }

    @GetMapping("/search")
    public ResponseEntity<List<FlightDTO>> searchFlights(
            @RequestParam String origin,
            @RequestParam String destination,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime departureDate) {

        List<Flight> flights = flightSearchService.searchFlights(origin, destination, departureDate);

        // Convert to DTO
        List<FlightDTO> flightDTOs = flights.stream().map(flight -> {
            FlightDTO dto = new FlightDTO();
            dto.setFlightNumber(flight.getFlightNumber());
            dto.setDepartureTime(flight.getDepartureTime());
            dto.setArrivalTime(flight.getArrivalTime());
            dto.setOriginAirportId(flight.getOrigin().getAirportId());
            dto.setDestinationAirportId(flight.getDestination().getAirportId());
            dto.setAircraftId(flight.getAircraft().getAircraftId());
            dto.setPrice(flight.getPrice());
            return dto;
        }).toList();

        return ResponseEntity.ok(flightDTOs);
    }

    // Admin-only endpoint to add a new flight
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<Flight> addFlight(@Valid @RequestBody FlightDTO flightDTO) {
        Flight flight = flightService.addFlight(flightDTO);
        return ResponseEntity.ok(flight);
    }
}
