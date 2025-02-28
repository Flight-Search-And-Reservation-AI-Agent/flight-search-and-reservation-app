package com.mycompany.flightapp.config;

import com.mycompany.flightapp.dto.FlightDTO;
import com.mycompany.flightapp.model.Aircraft;
import com.mycompany.flightapp.model.Airport;
import com.mycompany.flightapp.service.FlightService;
import com.mycompany.flightapp.repository.AirportRepository;
import com.mycompany.flightapp.repository.AircraftRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
public class FlightAppConfig {

    @Bean
    public CommandLineRunner demo(AirportRepository airportRepo, AircraftRepository aircraftRepo, FlightService flightService) {
        return args -> {
            // Save airports first
            Airport origin = new Airport("Delhi", "DEL", "Delhi", "India");
            origin = airportRepo.save(origin);
            Airport destination = airportRepo.save(new Airport("Mumbai", "BOM", "Mumbai", "India"));

            // Save aircraft
            Aircraft aircraft = aircraftRepo.save(new Aircraft("Boeing 777", 300));

            // Create DTO for Flight
            FlightDTO flightDTO = new FlightDTO();
            flightDTO.setFlightNumber("AI101");
            flightDTO.setDepartureTime(LocalDateTime.of(2025, 2, 6, 10, 0));
            flightDTO.setArrivalTime(LocalDateTime.of(2025, 2, 6, 13, 0));
            flightDTO.setOriginAirportId(origin.getAirportId());  // Ensure valid ID
            flightDTO.setDestinationAirportId(destination.getAirportId());
            flightDTO.setAircraftId(aircraft.getAircraftId());
            flightDTO.setPrice(5000.00);

            // Add flight using the service
            flightService.addFlight(flightDTO);
        };
    }
}

