package com.mycompany.flightapp.repository;

import com.mycompany.flightapp.model.PollOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PollOptionRepository extends JpaRepository<PollOption, String> {
    List<PollOption> findByPoll_PollId(String pollId);
}
