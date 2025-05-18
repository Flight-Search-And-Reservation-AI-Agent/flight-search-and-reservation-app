package com.mycompany.flightapp.controller;

import com.mycompany.flightapp.dto.PollDTO;
import com.mycompany.flightapp.model.Poll;
import com.mycompany.flightapp.service.PollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/trip-groups/{tripGroupId}/polls")
public class PollController {

    @Autowired
    private PollService pollService;

    @PostMapping("/create")
    public ResponseEntity<Poll> createPoll(
            @PathVariable String tripGroupId,
            @RequestBody PollDTO createPollRequest) {

        Poll createdPoll = pollService.createPoll(
                tripGroupId,
                createPollRequest.getQuestion(),
                createPollRequest.getOptions(),
                createPollRequest.isAnonymous()
        );
        return ResponseEntity.ok(createdPoll);
    }

    @GetMapping
    public ResponseEntity<List<Poll>> getPollsForTripGroup(@PathVariable String tripGroupId) {
        List<Poll> polls = pollService.getPollsForTripGroup(tripGroupId);
        return ResponseEntity.ok(polls);
    }

    // ✅ New voting endpoint
    @PostMapping("/{pollId}/vote/{optionId}")
    public ResponseEntity<String> voteOption(
            @PathVariable String tripGroupId,
            @PathVariable String pollId,
            @PathVariable String optionId,
            @RequestParam String userId) {

        pollService.voteOption(optionId, userId, pollId);
        return ResponseEntity.ok("Vote recorded successfully.");
    }

    @PutMapping("/{pollId}")
    public ResponseEntity<Poll> updatePoll(@PathVariable String pollId,
                                           @RequestBody Poll pollRequest) {
        Poll updated = pollService.updatePoll(pollId, pollRequest);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{pollId}")
    public ResponseEntity<Void> deletePoll(@PathVariable String pollId) {
        try {
            pollService.deletePoll(pollId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
