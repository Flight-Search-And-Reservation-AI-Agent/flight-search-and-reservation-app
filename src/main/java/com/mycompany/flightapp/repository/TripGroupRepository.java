package com.mycompany.flightapp.repository;

import com.mycompany.flightapp.model.TripGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TripGroupRepository extends JpaRepository<TripGroup, String> {
    List<TripGroup> findByMembers_UserId(String userId);
    Optional<TripGroup> findByTripName(String tripName);
}
