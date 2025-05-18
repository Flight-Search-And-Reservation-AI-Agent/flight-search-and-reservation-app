package com.mycompany.flightapp.repository;

import com.mycompany.flightapp.model.Poll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface PollRepository extends JpaRepository<Poll, String> {
    List<Poll> findByTripGroup_TripGroupId(String tripGroupId);
}
