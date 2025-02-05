package com.mycompany.flightapp.repository;

import com.mycompany.flightapp.model.Airport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AirportRepository extends JpaRepository<Airport, Long> {
    // Additional custom queries can be defined here if needed.
}

