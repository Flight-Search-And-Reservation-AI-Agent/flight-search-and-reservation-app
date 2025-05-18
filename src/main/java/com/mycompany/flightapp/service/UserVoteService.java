package com.mycompany.flightapp.service;

import com.mycompany.flightapp.model.UserVote;
import com.mycompany.flightapp.repository.UserVoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserVoteService {

    @Autowired
    private UserVoteRepository userVoteRepository;

    public List<UserVote> getVotesByUserId(String userId) {
        return userVoteRepository.findByUser_UserId(userId);
    }
}
