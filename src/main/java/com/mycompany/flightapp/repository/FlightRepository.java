package com.mycompany.flightapp.repository;

import com.mycompany.flightapp.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FlightRepository extends JpaRepository<Flight, String> {

    // Custom query: Search flights by airport codes and departure time range.
    List<Flight> findByOrigin_CodeAndDestination_CodeAndDepartureTimeBetween(
            String originCode,
            String destinationCode,
            LocalDateTime start,
            LocalDateTime end
    );
}
