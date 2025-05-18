package com.mycompany.flightapp.controller;

import com.mycompany.flightapp.model.UserVote;
import com.mycompany.flightapp.service.UserVoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user-vote")
public class UserVoteController {

    @Autowired
    private UserVoteService userVoteService;

    @GetMapping("/{userId}/votes")
    public ResponseEntity<List<UserVote>> getUserVotes(@PathVariable String userId) {
        List<UserVote> votes = userVoteService.getVotesByUserId(userId);
        return ResponseEntity.ok(votes);
    }
}
