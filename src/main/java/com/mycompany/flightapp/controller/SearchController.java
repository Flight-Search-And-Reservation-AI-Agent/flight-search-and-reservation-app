package com.mycompany.flightapp.controller;

import com.mycompany.flightapp.dto.FlightResponseDTO;
import com.mycompany.flightapp.model.Airport;
import com.mycompany.flightapp.model.Flight;
import com.mycompany.flightapp.service.AirportService;
import com.mycompany.flightapp.service.FlightService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/flight-results")
@Slf4j
public class SearchController {
    private final FlightService flightService;
    private final AirportService airportService;

    public SearchController(FlightService flightService, AirportService airportService) {
        this.flightService = flightService;
        this.airportService = airportService;
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchFlights(
            @RequestParam String origin,
            @RequestParam String destination,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime departureDate) {
        try{
            List<Flight> flights = flightService.searchFlights(origin, destination, departureDate);
            if (flights.isEmpty()) {
                log.warn("No flights found for search criteria: origin {}, destination {}, departureDate {}",
                        origin, destination, departureDate);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No flights found for the given search criteria.");
            }
            log.info("Flight search successful for origin: {}, destination: {}, departureDate: {}",
                    origin, destination, departureDate);
            // Convert to DTO
            List<FlightResponseDTO> flightDTOs = flights.stream().map(flight -> {
                FlightResponseDTO dto = new FlightResponseDTO();
                dto.setFlightId(flight.getFlightId());
                dto.setFlightNumber(flight.getFlightNumber());
                dto.setDepartureTime(flight.getDepartureTime());
                dto.setArrivalTime(flight.getArrivalTime());
                dto.setOriginAirportId(flight.getOrigin().getAirportId());
                dto.setOriginAirportName(flight.getOrigin().getName());
                dto.setDestinationAirportId(flight.getDestination().getAirportId());
                dto.setDestinationAirportName(flight.getDestination().getName());
                dto.setAirline(flight.getAircraft().getAirline());
                dto.setAircraftId(flight.getAircraft().getAircraftId());
                dto.setPrice(flight.getPrice());
                return dto;
            }).toList();

            return ResponseEntity.ok(flightDTOs);

        }
        catch (Exception e){
            log.error("Error searching flights for origin: {}, destination: {}, departureDate: {}",
                    origin, destination, departureDate, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error searching flights: " + e.getMessage());
        }
    }

    @GetMapping("/cities")
    public ResponseEntity<?> getAllAirports(){
        try{
            List<Airport> airportList=airportService.getAllAirports();
            log.info("Retrieved {} airports.",airportList.size());
            return ResponseEntity.ok(airportList);
        }
        catch (Exception e){
            log.error("Error retrieving airports:",e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
