package com.mycompany.flightapp.service;

import com.mycompany.flightapp.dto.ReservationDTO;
import com.mycompany.flightapp.exception.ResourceNotFoundException;
import com.mycompany.flightapp.model.Flight;
import com.mycompany.flightapp.model.Reservation;
import com.mycompany.flightapp.model.User;
import com.mycompany.flightapp.repository.FlightRepository;
import com.mycompany.flightapp.repository.ReservationRepository;
import com.mycompany.flightapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FlightRepository flightRepository;

    @InjectMocks
    private ReservationServiceImpl reservationService;


    private Reservation reservation;
    private ReservationDTO reservationDTO;
    private User user;
    private Flight flight;

    @BeforeEach
    void setUp(){
        user = new User();
        user.setUserId("user123");

        flight = new Flight();
        flight.setFlightId("1L");

        reservationDTO = new ReservationDTO("user123", "1L", "BOOKED","3A");

        reservation = new Reservation();
        reservation.setReservationId("res123");
        reservation.setUser(user);
        reservation.setFlight(flight);
        reservation.setStatus("CONFIRMED");
    }

    @Test
    void testCreateReservation_Success(){
        when(userRepository.findById(reservationDTO.getUserId())).thenReturn(Optional.of(user));
        when(flightRepository.findById(reservationDTO.getFlightId())).thenReturn(Optional.of(flight));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        Reservation createReservation = reservationService.createReservation(reservationDTO);

        assertNotNull(createReservation);
        assertEquals("CONFIRMED",createReservation.getStatus());
        verify(reservationRepository,times(1)).save(any(Reservation.class));
    }

    @Test
    void testCreateReservation_NotFound(){
        when(userRepository.findById(reservationDTO.getUserId())).thenReturn(Optional.empty());
        Exception exception = assertThrows(ResourceNotFoundException.class,()-> reservationService.createReservation(reservationDTO));

        assertEquals("User not found with id: user123",exception.getMessage());
        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    void testCreateReservation_FlightNotFound(){
        when(userRepository.findById(reservationDTO.getUserId())).thenReturn(Optional.of(user));
        when(flightRepository.findById(reservationDTO.getFlightId())).thenReturn(Optional.empty());
        Exception exception = assertThrows(ResourceNotFoundException.class,()-> reservationService.createReservation(reservationDTO));

        assertEquals("Flight not found with id: 1L",exception.getMessage());
        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    void testGetReservationForUser_Success(){
        when(userRepository.findById("user123")).thenReturn(Optional.of(user));

        when(reservationRepository.findReservationsByUserId("user123")).thenReturn(List.of(reservation));

        List<Reservation> reservations = reservationService.getReservationsForUser("user123");

        assertFalse(reservations.isEmpty());
        assertEquals(1, reservations.size());
        assertEquals("CONFIRMED", reservations.get(0).getStatus());
    }

    @Test
    void testGetReservationForUser_UserNotFound(){
        when(userRepository.findById("invalidUser")).thenReturn(Optional.empty());
        Exception exception = assertThrows(ResourceNotFoundException.class,()-> reservationService.getReservationsForUser("invalidUser"));

        assertEquals("User not found with id: invalidUser",exception.getMessage());
        verify(reservationRepository, never()).findReservationsByUserId(anyString());
    }

    @Test
    void testCancelReservation_Success(){
        when(reservationRepository.findById("res123")).thenReturn(Optional.of(reservation));
        boolean result =reservationService.cancelReservation("res123");
        assertTrue(result);
        assertEquals("CANCELLED", reservation.getStatus());
        verify(reservationRepository, times(1)).save(reservation);
    }

    @Test
    void testCancelReservation_NotFound(){
        when(reservationRepository.findById("invalidRes")).thenReturn(Optional.empty());
        boolean result =reservationService.cancelReservation("invalidRes");
        assertFalse(result);
        verify(reservationRepository, never()).save(any(Reservation.class));
    }

}
