package com.mycompany.flightapp.repository;

import com.mycompany.flightapp.model.Aircraft;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AircraftRepository extends JpaRepository<Aircraft, Long> {
    // Additional custom queries can be defined here if needed.
}
