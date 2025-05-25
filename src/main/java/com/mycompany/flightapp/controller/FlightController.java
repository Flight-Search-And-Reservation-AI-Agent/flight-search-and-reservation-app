package com.mycompany.flightapp.controller;

import com.mycompany.flightapp.dto.FlightDTO;
import com.mycompany.flightapp.dto.FlightResponseDTO;
import com.mycompany.flightapp.model.Flight;
import com.mycompany.flightapp.service.FlightService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/flights")
@Slf4j
public class FlightController {

    private final FlightService flightService;

    @Autowired
    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }


    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> getAllFlights() {
        List<FlightResponseDTO> flights = flightService.getAllFlights();
        return ResponseEntity.ok(flights);
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
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

    //Retrieve fligth details by id
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> getFlightById(@PathVariable String id){
        try{
            Flight flight=flightService.getFlightById(id);
            if(flight ==null){
                log.warn("Flight not found  with id: {}",id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Flight not found with id:" + id);

            }
            log.info("Retrieved flight: {} with id: {}", flight.getFlightNumber(),id);
            return ResponseEntity.ok(flight);
        }
        catch (Exception e){
            log.warn("Error retrieving flight with id: {}",id,e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving flight");
        }
    }


    // Admin-only endpoint to add a new flight
    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addFlight(@Valid @RequestBody FlightDTO flightDTO) {
        try{
            Flight flight = flightService.addFlight(flightDTO);
            log.info("Flight added successfully: {}", flight.getFlightNumber());
            return ResponseEntity.status(HttpStatus.CREATED).body(flight);
        }catch (Exception e){
            log.error("Error adding Flight with flight number {}: {}",flightDTO.getFlightNumber(),e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to add flight: " + e.getMessage());
        }
    }

    // Update flight Info
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateFlight(@PathVariable String id, @Valid @RequestBody FlightDTO flightDTO){
        try{
            Flight updatedFlight =flightService.updateFlight(id,flightDTO);
            if(updatedFlight ==null){
                log.warn("Flight not found for update with id: {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("flight not found with id:" + id);

            }
            log.info("Flight updated successfully: {} with id: {}", updatedFlight.getFlightNumber(), id);
            return ResponseEntity.ok(updatedFlight);
        }
        catch (Exception e){
            log.warn("Error updating flight with id: {}", id,e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating flight");
        }
    }

    //Delete flight by id
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteFlight(@PathVariable String id){
        try{
            boolean deleted= flightService.deleteFlight(id);
            if(!deleted){
                log.warn("Flight not found for deletion with id: {}",id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Flight not found with id:" + id);

            }
            log.info("Flight deleted successfully with id: {}",id);
            return ResponseEntity.noContent().build();
        }
        catch (Exception e){
            log.warn("Error deleting flight with id: {}",id,e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting flight");
        }
    }
}
