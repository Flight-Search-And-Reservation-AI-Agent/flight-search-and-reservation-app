package com.mycompany.flightapp.service;

import com.mycompany.flightapp.dto.ReservationDTO;
import com.mycompany.flightapp.model.Reservation;

import java.util.List;

public interface ReservationService {
    Reservation createReservation(ReservationDTO reservationDTO);
    void cancelReservation(String reservationId);
    List<Reservation> getReservationsForUser(String userId);
}
