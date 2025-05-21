package com.mycompany.flightapp.service;

import com.mycompany.flightapp.dto.OptionDTO;
import com.mycompany.flightapp.dto.PollUpdateMessage;
import com.mycompany.flightapp.model.*;
import com.mycompany.flightapp.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PollServiceImpl implements PollService {

    @Autowired
    private PollRepository pollRepository;

    @Autowired
    private PollOptionRepository pollOptionRepository;

    @Autowired
    private TripGroupRepository tripGroupRepository;

    @Autowired
    private UserVoteRepository userVoteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;


    @Override
    public Poll createPoll(String tripGroupId, String question, List<String> options, boolean anonymous) {
        TripGroup tripGroup = tripGroupRepository.findById(tripGroupId)
                .orElseThrow(() -> new RuntimeException("Trip group not found"));

        Poll poll = Poll.builder()
                .question(question)
                .tripGroup(tripGroup)
                .anonymous(anonymous)
                .build();

        List<PollOption> pollOptions = options.stream()
                .map(optionText -> PollOption.builder()
                        .optionText(optionText)
                        .voteCount(0)
                        .poll(poll)
                        .build())
                .collect(Collectors.toList());

        poll.setOptions(pollOptions);

        return pollRepository.save(poll);
    }

    @Override
    public List<Poll> getPollsForTripGroup(String tripGroupId) {
        return pollRepository.findByTripGroup_TripGroupId(tripGroupId);
    }

    @Transactional
    @Override
    public void voteOption(String optionId, String userId, String pollId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Poll poll = pollRepository.findById(pollId)
                .orElseThrow(() -> new RuntimeException("Poll not found"));

        PollOption selectedOption = pollOptionRepository.findById(optionId)
                .orElseThrow(() -> new RuntimeException("Option not found"));

        UserVote existingVote = userVoteRepository.findByUser_UserIdAndPoll_PollId(userId, pollId);

        if (existingVote != null) {
            PollOption previousOption = existingVote.getPollOption();
            previousOption.setVoteCount(previousOption.getVoteCount() - 1);
            pollOptionRepository.save(previousOption);

            selectedOption.setVoteCount(selectedOption.getVoteCount() + 1);
            pollOptionRepository.save(selectedOption);

            existingVote.setPollOption(selectedOption);
            userVoteRepository.save(existingVote);
        } else {
            selectedOption.setVoteCount(selectedOption.getVoteCount() + 1);
            pollOptionRepository.save(selectedOption);

            UserVote vote = UserVote.builder()
                    .user(user)
                    .poll(poll)
                    .pollOption(selectedOption)
                    .build();
            userVoteRepository.save(vote);
        }

        List<OptionDTO> updatedOptions = pollOptionRepository.findByPoll_PollId(pollId)
                .stream()
                .map(option -> new OptionDTO(
                        option.getOptionId(),
                        option.getOptionText(),
                        option.getVoteCount()
                ))
                .collect(Collectors.toList());

        // Retrieve the trip group ID from the poll
        String tripGroupId = poll.getTripGroup().getTripGroupId();

        // Send real-time update using trip group ID as topic
        PollUpdateMessage updateMessage = new PollUpdateMessage(pollId, updatedOptions);
        messagingTemplate.convertAndSend("/topic/poll-updates/" + tripGroupId, updateMessage);
    }


    @Override
    public Poll updatePoll(String pollId, Poll pollRequest) {
        Poll existing = pollRepository.findById(pollId)
                .orElseThrow(() -> new RuntimeException("Poll not found"));
        existing.setQuestion(pollRequest.getQuestion());
        existing.setAnonymous(pollRequest.isAnonymous());
        return pollRepository.save(existing);
    }

    @Override
    public void deletePoll(String pollId) {
        pollRepository.deleteById(pollId);
    }
}
