package com.mycompany.flightapp.repository;

import com.mycompany.flightapp.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    // Additional query methods can be defined here if needed
}

