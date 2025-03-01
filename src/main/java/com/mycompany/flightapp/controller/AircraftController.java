package com.mycompany.flightapp.controller;

import com.mycompany.flightapp.model.Aircraft;
import com.mycompany.flightapp.service.AircraftService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/aircrafts")
@Slf4j
public class AircraftController {
    private final AircraftService aircraftService;

    @Autowired
    public AircraftController(AircraftService aircraftService) {
        this.aircraftService = aircraftService;
    }

    @GetMapping
    public ResponseEntity<?> getAllAirports(){
        try{
            List<Aircraft> aircraftList=aircraftService.getAllAircrafts();
            log.info("Retrieved {} aircrafts.",aircraftList.size());
            return ResponseEntity.ok(aircraftList);
        }
        catch (Exception e){
            log.error("Error retrieving aircrafts:",e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{aircraftId}")
    public ResponseEntity<?> getAirportById(@PathVariable String aircraftId){
        try {
            Optional<Aircraft> aircraftOptional = aircraftService.getAircraftWithId(aircraftId);
            if(aircraftOptional.isPresent()){
                log.info("Retrieved aircraft with id: {}",aircraftId);
                return ResponseEntity.ok(aircraftOptional.get());
            }else {
                log.warn("Aircraft not found with id: {}",aircraftId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Aircraft not found with id: "+aircraftId);
            }
        }
        catch (Exception e){
            log.error("Error retrieving aircraft with id: {}",aircraftId,e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving aircraft");
        }
    }

    @PostMapping
    public ResponseEntity<?> createAirport(@RequestBody Aircraft aircraft){
        try {
            Aircraft created = aircraftService.createAircraft(aircraft);
            log.info("Created aircraft with id: {}",created.getAircraftId());
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        }
        catch (Exception e){
            log.error("Error retrieving aircraft",e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error creating aircraft: "+e.getMessage());
        }
    }

    @PutMapping("/{aircraftId}")
    public ResponseEntity<?> updatedAirport(@PathVariable String aircraftId,@RequestBody Aircraft aircraft){
        try{
            Aircraft updateAircraft = aircraftService.updateAircraft(aircraftId,aircraft);
            if(updateAircraft!=null){
                log.info("Updated aircraft with id: {}",aircraftId);
                return ResponseEntity.ok(updateAircraft);
            }else {
                log.warn("Aircraft not found for updating with id: {}",aircraftId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Aircraft not found with id: "+aircraftId);
            }
        }
        catch (Exception e){
            log.error("Error updating aircraft with id: {}",aircraftId,e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating aircraft");
        }
    }

    @DeleteMapping("/{aircraftId}")
    public ResponseEntity<?> deletedAirport(@PathVariable String aircraftId){
        try{
            boolean deleteAirport = aircraftService.deleteAircraft(aircraftId);
            if (deleteAirport){
                log.info("Deleted aircraft with id: {}",aircraftId);
                return ResponseEntity.noContent().build();
            }else {
                log.warn("Airport not found for deletion with id: {}",aircraftId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Airport not found with id: "+aircraftId);
            }
        }
        catch (Exception e){
            log.error("Error deleting aircraft with id: {}",aircraftId,e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting aircraft");
        }
    }
}
