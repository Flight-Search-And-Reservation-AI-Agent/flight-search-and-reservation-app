package com.mycompany.flightapp.service;

import com.mycompany.flightapp.dto.OptionDTO;
import com.mycompany.flightapp.dto.PollUpdateMessage;
import com.mycompany.flightapp.model.*;
import com.mycompany.flightapp.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PollServiceImplTest {

    @Mock private PollRepository pollRepository;
    @Mock private PollOptionRepository pollOptionRepository;
    @Mock private TripGroupRepository tripGroupRepository;
    @Mock private UserVoteRepository userVoteRepository;
    @Mock private UserRepository userRepository;
    @Mock private SimpMessagingTemplate messagingTemplate;

    @InjectMocks private PollServiceImpl pollService;

    private TripGroup tripGroup;
    private Poll poll;
    private PollOption option1;
    private PollOption option2;
    private User user;

    @BeforeEach
    void setUp() {
        tripGroup = TripGroup.builder().tripGroupId("tg1").build();
        poll = Poll.builder().pollId("p1").question("Where to go?").tripGroup(tripGroup).anonymous(false).build();
        option1 = PollOption.builder().optionId("o1").optionText("Paris").voteCount(0).poll(poll).build();
        option2 = PollOption.builder().optionId("o2").optionText("Tokyo").voteCount(0).poll(poll).build();
        user = User.builder().userId("u1").username("Alice").build();
    }

    @Test
    void testCreatePoll_Success() {
        List<String> options = Arrays.asList("Paris", "Tokyo");
        when(tripGroupRepository.findById("tg1")).thenReturn(Optional.of(tripGroup));
        when(pollRepository.save(any(Poll.class))).thenAnswer(i -> i.getArguments()[0]);

        Poll createdPoll = pollService.createPoll("tg1", "Where to go?", options, false);

        assertNotNull(createdPoll);
        assertEquals("Where to go?", createdPoll.getQuestion());
        assertEquals(2, createdPoll.getOptions().size());
        verify(pollRepository).save(any(Poll.class));
    }

    @Test
    void testGetPollsForTripGroup() {
        when(pollRepository.findByTripGroup_TripGroupId("tg1")).thenReturn(List.of(poll));
        List<Poll> polls = pollService.getPollsForTripGroup("tg1");

        assertEquals(1, polls.size());
        verify(pollRepository).findByTripGroup_TripGroupId("tg1");
    }

    @Test
    void testVoteOption_NewVote() {
        when(userRepository.findById("u1")).thenReturn(Optional.of(user));
        when(pollRepository.findById("p1")).thenReturn(Optional.of(poll));
        when(pollOptionRepository.findById("o1")).thenReturn(Optional.of(option1));
        when(userVoteRepository.findByUser_UserIdAndPoll_PollId("u1", "p1")).thenReturn(null);
        when(pollOptionRepository.findByPoll_PollId("p1")).thenReturn(List.of(option1));

        pollService.voteOption("o1", "u1", "p1");

        assertEquals(1, option1.getVoteCount());
        verify(pollOptionRepository).save(option1);
        verify(userVoteRepository).save(any(UserVote.class));
        verify(messagingTemplate).convertAndSend(eq("/topic/poll-updates/tg1"), any(PollUpdateMessage.class));
    }

    @Test
    void testVoteOption_ChangeVote() {
        option1.setVoteCount(1);
        option2.setVoteCount(0);
        UserVote existingVote = UserVote.builder().user(user).poll(poll).pollOption(option1).build();

        when(userRepository.findById("u1")).thenReturn(Optional.of(user));
        when(pollRepository.findById("p1")).thenReturn(Optional.of(poll));
        when(pollOptionRepository.findById("o2")).thenReturn(Optional.of(option2));
        when(userVoteRepository.findByUser_UserIdAndPoll_PollId("u1", "p1")).thenReturn(existingVote);
        when(pollOptionRepository.findByPoll_PollId("p1")).thenReturn(List.of(option1, option2));

        pollService.voteOption("o2", "u1", "p1");

        assertEquals(0, option1.getVoteCount());
        assertEquals(1, option2.getVoteCount());
        verify(pollOptionRepository, times(2)).save(any(PollOption.class));
        verify(userVoteRepository).save(existingVote);
        verify(messagingTemplate).convertAndSend(eq("/topic/poll-updates/tg1"), any(PollUpdateMessage.class));
    }

    @Test
    void testUpdatePoll() {
        Poll update = Poll.builder().question("New Question").anonymous(true).build();
        when(pollRepository.findById("p1")).thenReturn(Optional.of(poll));
        when(pollRepository.save(any(Poll.class))).thenAnswer(i -> i.getArguments()[0]);

        Poll updated = pollService.updatePoll("p1", update);

        assertEquals("New Question", updated.getQuestion());
        assertTrue(updated.isAnonymous());
    }

    @Test
    void testDeletePoll() {
        pollService.deletePoll("p1");
        verify(pollRepository).deleteById("p1");
    }
}
