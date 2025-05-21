package com.mycompany.flightapp.service;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PresenceService {

    // tripGroupId -> Set of usernames who are online
    private final Map<String, Set<String>> onlineUsersByGroup = new ConcurrentHashMap<>();

    public void userConnected(String tripGroupId, String username) {
        onlineUsersByGroup
                .computeIfAbsent(tripGroupId, k -> ConcurrentHashMap.newKeySet())
                .add(username);
    }

    public void userDisconnected(String tripGroupId, String username) {
        Set<String> users = onlineUsersByGroup.get(tripGroupId);
        if (users != null) {
            users.remove(username);
            if (users.isEmpty()) {
                onlineUsersByGroup.remove(tripGroupId);
            }
        }
    }

    public Set<String> getOnlineUsers(String tripGroupId) {
        return onlineUsersByGroup.getOrDefault(tripGroupId, Collections.emptySet());
    }
}

