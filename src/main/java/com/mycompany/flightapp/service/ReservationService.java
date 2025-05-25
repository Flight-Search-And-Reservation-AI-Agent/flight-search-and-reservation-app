package com.mycompany.flightapp.service;

import com.mycompany.flightapp.dto.CreateReservationRequest;
import com.mycompany.flightapp.dto.ReservationDTO;
import com.mycompany.flightapp.model.Reservation;

import java.util.List;

public interface ReservationService {
    Reservation createReservation(CreateReservationRequest request);
    boolean cancelReservation(String reservationId);
    List<Reservation> getReservationsForUser(String userId);
    List<Reservation> getAllReservations();
    public Reservation updateReservation(String reservationId, Reservation updatedData);

}
