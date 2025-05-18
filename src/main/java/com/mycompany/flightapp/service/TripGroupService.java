package com.mycompany.flightapp.service;


import com.mycompany.flightapp.model.TripGroup;
import com.mycompany.flightapp.dto.TripGroupDTO;
import org.springframework.stereotype.Service;

import java.util.List;


public interface TripGroupService {
    TripGroup createGroup(TripGroupDTO groupDTO, String creatorUserId);
    List<TripGroup> getUserGroups(String userId);
    void deleteGroup(String tripGroupId, String userId);
    TripGroup getGroupById(String tripGroupId);
    void addMemberToGroup(String groupId, String userId);
}
