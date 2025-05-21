package com.mycompany.flightapp.repository;

import com.mycompany.flightapp.model.TripNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface TripNoteRepository extends JpaRepository<TripNote, String> {
    List<TripNote> findByTripGroup_TripGroupId(String tripGroupId);
}
