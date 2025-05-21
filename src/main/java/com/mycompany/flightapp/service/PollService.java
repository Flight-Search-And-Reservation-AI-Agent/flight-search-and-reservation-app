package com.mycompany.flightapp.service;

import com.mycompany.flightapp.model.Poll;
import com.mycompany.flightapp.model.PollOption;

import java.util.List;

public interface PollService {
    Poll createPoll(String tripGroupId, String question, List<String> options, boolean anonymous);

    List<Poll> getPollsForTripGroup(String tripGroupId);

    void voteOption(String userId, String pollId, String optionId);

    Poll updatePoll(String pollId, Poll pollRequest);

    void deletePoll(String pollId);
}
