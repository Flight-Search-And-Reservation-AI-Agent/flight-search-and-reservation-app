package com.mycompany.flightapp.service;

import com.mycompany.flightapp.dto.TripGroupDTO;
import com.mycompany.flightapp.model.TripGroup;
import com.mycompany.flightapp.model.User;
import com.mycompany.flightapp.repository.TripGroupRepository;
import com.mycompany.flightapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TripGroupServiceImpl implements TripGroupService {

    private final TripGroupRepository tripGroupRepository;
    private final UserRepository userRepository;

    @Override
    public TripGroup createGroup(TripGroupDTO groupDTO, String creatorUserId) {
        Optional<TripGroup> existingGroup = tripGroupRepository.findByTripName(groupDTO.getTripName());
        if (existingGroup.isPresent()) {
            throw new RuntimeException("Trip group with the same name already exists!");
        }
        User creator = userRepository.findById(creatorUserId)
                .orElseThrow(() -> new RuntimeException("Creator not found"));

        TripGroup group = TripGroup.builder()
                .tripName(groupDTO.getTripName())
                .tripDescription(groupDTO.getTripDescription())
                .tripAvatarUrl(groupDTO.getTripAvatarUrl())
                .status(groupDTO.getStatus())
                .dates(groupDTO.getDates())
                .createdBy(creator)
                .members(List.of(creator))
                .build();

        return tripGroupRepository.save(group);
    }

    @Override
    public List<TripGroup> getUserGroups(String userId) {
        return tripGroupRepository.findByMembers_UserId(userId);
    }
    @Override
    public void deleteGroup(String tripGroupId, String userId) {
        // Fetch the group by its ID
        TripGroup group = tripGroupRepository.findById(tripGroupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        // Check if the user is the creator of the group
        if (!group.getCreatedBy().getUserId().equals(userId)) {
            throw new RuntimeException("Only the group creator can delete the group");
        }

        // If the user is the creator, delete the group
        tripGroupRepository.deleteById(tripGroupId);
    }

    public TripGroup getGroupById(String tripGroupId) {
        return tripGroupRepository.findById(tripGroupId)
                .orElseThrow(() -> new RuntimeException("Trip group not found with id: " + tripGroupId));
    }

    public void addMemberToGroup(String groupId, String userId) {
        TripGroup group = tripGroupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!group.getMembers().contains(user)) {
            group.getMembers().add(user);
            tripGroupRepository.save(group);
        }
    }


}

