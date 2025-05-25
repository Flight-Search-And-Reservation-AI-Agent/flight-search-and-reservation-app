package com.mycompany.flightapp.service;

import com.mycompany.flightapp.dto.CreateReservationRequest;
import com.mycompany.flightapp.dto.ReservationDTO;
import com.mycompany.flightapp.exception.ResourceNotFoundException;
import com.mycompany.flightapp.model.Flight;
import com.mycompany.flightapp.model.Passenger;
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
    public Reservation createReservation(CreateReservationRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Flight flight = flightRepository.findById(request.getFlightId())
                .orElseThrow(() -> new RuntimeException("Flight not found"));

        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setFlight(flight);
        reservation.setStatus("CONFIRMED");
        reservation.setReservationTime(LocalDateTime.now());

        // Convert PassengerDTO to Passenger entity
        List<Passenger> passengers = request.getPassengers().stream()
                .map(dto -> new Passenger(dto.getName(), dto.getAge(), dto.getGender()))
                .collect(Collectors.toList());

        reservation.setPassengers(passengers);

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

