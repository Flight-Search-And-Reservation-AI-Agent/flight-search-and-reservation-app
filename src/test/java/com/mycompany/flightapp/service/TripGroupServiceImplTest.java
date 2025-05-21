package com.mycompany.flightapp.service;


import com.mycompany.flightapp.dto.TripGroupDTO;
import com.mycompany.flightapp.model.TripGroup;
import com.mycompany.flightapp.model.User;
import com.mycompany.flightapp.repository.TripGroupRepository;
import com.mycompany.flightapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TripGroupServiceImplTest {

    @Mock
    private TripGroupRepository tripGroupRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TripGroupServiceImpl tripGroupService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private TripGroupDTO createSampleDTO() {
        TripGroupDTO dto = new TripGroupDTO();
        dto.setTripName("Goa Trip");
        dto.setTripDescription("Chilling in Goa");
        dto.setTripAvatarUrl("http://avatar.jpg");
        dto.setStatus("PLANNED");
        dto.setDates(List.of("2025-06-10", "2025-06-15").toString());
        return dto;
    }

    private User createSampleUser(String id) {
        return User.builder().userId(id).username("Divay").build();
    }

    private TripGroup createSampleGroup(String id, User user) {
        return TripGroup.builder()
                .tripName("Goa Trip")
                .tripDescription("Chilling in Goa")
                .tripAvatarUrl("http://avatar.jpg")
                .status("PLANNED")
                .dates(List.of("2025-06-10", "2025-06-15").toString())
                .createdBy(user)
                .members(new ArrayList<>(List.of(user)))
                .build();
    }

    @Test
    void testCreateGroup_Success() {
        TripGroupDTO dto = createSampleDTO();
        User creator = createSampleUser("user1");

        when(tripGroupRepository.findByTripName("Goa Trip")).thenReturn(Optional.empty());
        when(userRepository.findById("user1")).thenReturn(Optional.of(creator));
        when(tripGroupRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        TripGroup created = tripGroupService.createGroup(dto, "user1");

        assertNotNull(created);
        assertEquals("Goa Trip", created.getTripName());
        verify(tripGroupRepository).save(any());
    }

    @Test
    void testCreateGroup_TripNameAlreadyExists() {
        TripGroupDTO dto = createSampleDTO();
        when(tripGroupRepository.findByTripName("Goa Trip")).thenReturn(Optional.of(mock(TripGroup.class)));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> tripGroupService.createGroup(dto, "user1"));

        assertEquals("Trip group with the same name already exists!", ex.getMessage());
    }

    @Test
    void testGetUserGroups() {
        List<TripGroup> groups = List.of(mock(TripGroup.class));
        when(tripGroupRepository.findByMembers_UserId("user1")).thenReturn(groups);

        List<TripGroup> result = tripGroupService.getUserGroups("user1");

        assertEquals(1, result.size());
    }

    @Test
    void testDeleteGroup_AsCreator_Success() {
        User creator = createSampleUser("user1");
        TripGroup group = createSampleGroup("tg1", creator);
        when(tripGroupRepository.findById("tg1")).thenReturn(Optional.of(group));

        tripGroupService.deleteGroup("tg1", "user1");

        verify(tripGroupRepository).deleteById("tg1");
    }

    @Test
    void testDeleteGroup_NotCreator_ThrowsException() {
        User creator = createSampleUser("user1");
        TripGroup group = createSampleGroup("tg1", creator);
        when(tripGroupRepository.findById("tg1")).thenReturn(Optional.of(group));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> tripGroupService.deleteGroup("tg1", "otherUser"));

        assertEquals("Only the group creator can delete the group", ex.getMessage());
    }

    @Test
    void testGetGroupById_Found() {
        TripGroup group = mock(TripGroup.class);
        when(tripGroupRepository.findById("tg1")).thenReturn(Optional.of(group));

        TripGroup result = tripGroupService.getGroupById("tg1");
        assertNotNull(result);
    }

    @Test
    void testGetGroupById_NotFound() {
        when(tripGroupRepository.findById("tg1")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> tripGroupService.getGroupById("tg1"));

        assertEquals("Trip group not found with id: tg1", ex.getMessage());
    }

    @Test
    void testAddMemberToGroup_Success() {
        User creator = createSampleUser("user1");
        User newMember = createSampleUser("user2");
        TripGroup group = createSampleGroup("tg1", creator);

        when(tripGroupRepository.findById("tg1")).thenReturn(Optional.of(group));
        when(userRepository.findById("user2")).thenReturn(Optional.of(newMember));
        when(tripGroupRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        tripGroupService.addMemberToGroup("tg1", "user2");

        assertTrue(group.getMembers().contains(newMember));
        verify(tripGroupRepository).save(group);
    }

    @Test
    void testAddMemberToGroup_UserAlreadyMember() {
        User creator = createSampleUser("user1");
        TripGroup group = createSampleGroup("tg1", creator);

        when(tripGroupRepository.findById("tg1")).thenReturn(Optional.of(group));
        when(userRepository.findById("user1")).thenReturn(Optional.of(creator));

        tripGroupService.addMemberToGroup("tg1", "user1");

        // Should not save again
        verify(tripGroupRepository, never()).save(any());
    }
}
