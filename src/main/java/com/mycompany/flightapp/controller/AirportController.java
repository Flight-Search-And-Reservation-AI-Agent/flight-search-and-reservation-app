package com.mycompany.flightapp.controller;

import com.mycompany.flightapp.model.Airport;
import com.mycompany.flightapp.service.AirportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/airports")
@Slf4j
public class AirportController {
    private final AirportService airportService;

    @Autowired
    public AirportController(AirportService airportService) {
        this.airportService = airportService;
    }

    @GetMapping
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

    @GetMapping("/{airportId}")
    public ResponseEntity<?> getAirportById(@PathVariable String airportId){
        try {
            Optional<Airport> airportOpt = airportService.getAirportById(airportId);
            if(airportOpt.isPresent()){
                log.info("Retrieved airport with id: {}",airportId);
                return ResponseEntity.ok(airportOpt.get());
            }else {
                log.warn("Airport not found with id: {}",airportId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Airport not found with id: "+airportId);
            }
        }
        catch (Exception e){
            log.error("Error retrieving airport with id: {}",airportId,e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving airports");
        }
    }

    @PostMapping
    public ResponseEntity<?> createAirport(@RequestBody Airport airport){
        try {
            Airport created = airportService.createAirport(airport);
            log.info("Created airport with id: {}",created.getAirportId());
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        }
        catch (Exception e){
            log.error("Error retrieving airport",e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error creating airport: "+e.getMessage());
        }
    }

    @PutMapping("/{airportId}")
    public ResponseEntity<?> updatedAirport(@PathVariable String airportId,@RequestBody Airport airport){
        try{
            Airport updateAirport = airportService.updateAirport(airportId, airport);
            if(updateAirport!=null){
                log.info("Updated airport with id: {}",airportId);
                return ResponseEntity.ok(updateAirport);
            }else {
                log.warn("Airport not found for updating with id: {}",airportId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Airport not found with id: "+airportId);
            }
        }
        catch (Exception e){
            log.error("Error updating airport with id: {}",airportId,e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating airport");
        }
    }

    @DeleteMapping("/{airportId}")
    public ResponseEntity<?> deletedAirport(@PathVariable String airportId){
        try{
            boolean deleteAirport = airportService.deleteAirport(airportId);
            if (deleteAirport){
                log.info("Deleted airport with id: {}",airportId);
                return ResponseEntity.noContent().build();
            }else {
                log.warn("Airport not found for deletion with id: {}",airportId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Airport not found with id: "+airportId);

            }
        }
        catch (Exception e){
            log.error("Error deleting airport with id: {}",airportId,e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting airport");
        }
    }
}
