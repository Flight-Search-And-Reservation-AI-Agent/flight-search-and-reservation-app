package com.mycompany.flightapp.controller;

import com.mycompany.flightapp.dto.ReservationDTO;
import com.mycompany.flightapp.model.Reservation;
import com.mycompany.flightapp.service.ReservationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reservations")
@Slf4j
public class ReservationController {

    private final ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    // Endpoint to retrieve reservations for a user by their UUID
    @GetMapping
    public ResponseEntity<?> getReservationsForUser(@RequestParam String userId) {
        try{
            List<Reservation> reservations = reservationService.getReservationsForUser(userId);
            if(reservations.isEmpty()){
                log.warn("No reservation for user id: {}",userId);
                return ResponseEntity.status(404).body("No reservations found for user: " + userId);
            }
            return ResponseEntity.ok(reservations);
        }
        catch (Exception e){
            log.error("Error retrieving reservations for userId: {}",userId,e);
            return ResponseEntity.status(500).body("Internal server error while retrieving reservations");
        }

    }

    @PostMapping
    public ResponseEntity<?> createReservation(@RequestBody ReservationDTO reservationDTO) {

        try{
            Reservation reservation = reservationService.createReservation(reservationDTO);
            return ResponseEntity.ok(reservation);
        }catch (Exception e) {
            log.error("Error creating reservation for data: {}", reservationDTO, e);
            return ResponseEntity.status(400).body("Failed to create reservation: " + e.getMessage());
        }
    }

    @DeleteMapping("/{reservationId}")
    public ResponseEntity<?> cancelReservation(@PathVariable String reservationId) {
        try{
            boolean canceled= reservationService.cancelReservation(reservationId);
            if(canceled){
                log.warn("No reservation for reservation id: {}",reservationId);
                return ResponseEntity.status(404).body("No reservations found : " + reservationId);
            }
            return ResponseEntity.noContent().build();
        }
        catch (Exception e){
            log.error("Error cancelling reservation with id: {}", reservationId, e);
            return ResponseEntity.status(500).body("Internal server error while canceling reservations");
        }


    }
}
