package com.mycompany.flightapp.service;

import com.mycompany.flightapp.dto.ReservationDTO;
import com.mycompany.flightapp.model.Reservation;

public interface ReservationService {
    Reservation createReservation(ReservationDTO reservationDTO);
    void cancelReservation(Long reservationId);
}
