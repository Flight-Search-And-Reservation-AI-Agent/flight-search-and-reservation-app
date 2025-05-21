package com.mycompany.flightapp.service;

import com.mycompany.flightapp.dto.ReservationDTO;
import com.mycompany.flightapp.exception.ResourceNotFoundException;
import com.mycompany.flightapp.model.Flight;
import com.mycompany.flightapp.model.Reservation;
import com.mycompany.flightapp.model.User;
import com.mycompany.flightapp.repository.ReservationRepository;
import com.mycompany.flightapp.repository.FlightRepository;
import com.mycompany.flightapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final FlightRepository flightRepository;
    private final UserRepository userRepository;

    @Autowired
    public ReservationServiceImpl(ReservationRepository reservationRepository,
                                  FlightRepository flightRepository,
                                  UserRepository userRepository) {
        this.reservationRepository = reservationRepository;
        this.flightRepository = flightRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Reservation createReservation(ReservationDTO reservationDTO) {
        // Fetch the User entity based on reservationDTO.userId
        User user = userRepository.findById(reservationDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + reservationDTO.getUserId()));

        // Fetch the Flight entity based on reservationDTO.flightId
        Flight flight = flightRepository.findById(reservationDTO.getFlightId())
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found with id: " + reservationDTO.getFlightId()));

        // Create a new Reservation entity
        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setFlight(flight);
        reservation.setSeatNumber(reservationDTO.getSeatNumber());
        reservation.setStatus("BOOKED");
        reservation.setReservationTime(LocalDateTime.now());

        // Save the reservation
        return reservationRepository.save(reservation);
    }

    @Override
    public boolean cancelReservation(String reservationId) {
        // Retrieve the reservation, throw an exception if not found
        Optional<Reservation> optionalReservation = reservationRepository.findById(reservationId);
        if(!optionalReservation.isPresent()){
            return false;
        }
        Reservation reservation=optionalReservation.get();
        // Update status to "CANCELLED"
        reservation.setStatus("CANCELLED");
        reservationRepository.save(reservation);
        return  true;
    }


    @Override
    public List<Reservation> getReservationsForUser(String userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        return reservationRepository.findReservationsByUserId(userId);
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public Reservation updateReservation(String reservationId, Reservation updatedData) {
        return reservationRepository.findById(reservationId).map(existing -> {
            existing.setStatus(updatedData.getStatus());
            existing.setSeatNumber(updatedData.getSeatNumber());
            // You can update more fields as needed
            return reservationRepository.save(existing);
        }).orElse(null);
    }


}

