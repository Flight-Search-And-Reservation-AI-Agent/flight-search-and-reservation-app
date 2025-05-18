package com.mycompany.flightapp.repository;

import com.mycompany.flightapp.model.UserVote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserVoteRepository extends JpaRepository<UserVote, String> {
    UserVote findByUser_UserIdAndPoll_PollId(String userId, String pollId);
    List<UserVote> findByUser_UserId(String userId);
}
