package com.mycompany.flightapp.controller;

import com.mycompany.flightapp.dto.ReservationDTO;
import com.mycompany.flightapp.model.Reservation;
import com.mycompany.flightapp.service.ReservationService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v1/reservations")
@Slf4j
public class ReservationController {

    private final ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }


    @GetMapping
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllReservations() {
        try {
            List<Reservation> reservations = reservationService.getAllReservations();
            return ResponseEntity.ok(reservations);
        } catch (Exception e) {
            log.error("Error fetching all reservations", e);
            return ResponseEntity.status(500).body(null);
        }
    }

    @PutMapping("/{reservationId}")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateReservation(
            @PathVariable String reservationId,
            @RequestBody Reservation updatedReservationData) {
        try {
            Reservation updated = reservationService.updateReservation(reservationId, updatedReservationData);
            if (updated != null) {
                return ResponseEntity.ok(updated);
            } else {
                return ResponseEntity.status(404).body("Reservation not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body("Error updating reservation: " + e.getMessage());
        }
    }


    // Endpoint to retrieve reservations for a user by their UUID
    @GetMapping("/{userId}")
//    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<?> getReservationsForUser(@PathVariable String userId) {
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
//    @PreAuthorize("hasRole('ADMIN')")
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
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> cancelReservation(@PathVariable String reservationId) {
        try{
            boolean canceled= reservationService.cancelReservation(reservationId);
            if(!canceled){
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
