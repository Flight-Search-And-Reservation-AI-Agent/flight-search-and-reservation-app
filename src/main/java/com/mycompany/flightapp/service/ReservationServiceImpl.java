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
    public void cancelReservation(String reservationId) {
        // Retrieve the reservation, throw an exception if not found
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id: " + reservationId));

        // Update status to "CANCELLED"
        reservation.setStatus("CANCELLED");
        reservationRepository.save(reservation);
    }


    @Override
    public List<Reservation> getReservationsForUser(String userId) {
        return reservationRepository.findReservationsByUserId(userId);
    }
}

