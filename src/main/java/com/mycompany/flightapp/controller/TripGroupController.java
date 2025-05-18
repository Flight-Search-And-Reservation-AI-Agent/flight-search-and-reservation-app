package com.mycompany.flightapp.controller;

import com.mycompany.flightapp.dto.TripGroupDTO;
import com.mycompany.flightapp.model.TripGroup;
import com.mycompany.flightapp.service.TripGroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/trip-groups")
@RequiredArgsConstructor
@Slf4j
public class TripGroupController {

    private final TripGroupService tripGroupService;

        @PostMapping
        public ResponseEntity<?> createGroup(@RequestBody TripGroupDTO groupDTO,
                                             @RequestParam String creatorUserId) {
            try {
                TripGroup createdGroup = tripGroupService.createGroup(groupDTO, creatorUserId);
                log.info("Trip group '{}' created successfully by user '{}'", createdGroup.getTripName(), creatorUserId);
                return new ResponseEntity<>(createdGroup, HttpStatus.CREATED);
            } catch (RuntimeException e) {  // This will catch group already exists etc
                log.warn("Failed to create trip group '{}'. Reason: {}", groupDTO.getTripName(), e.getMessage());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            } catch (Exception e) {
                log.error("Unexpected error while creating trip group '{}'.", groupDTO.getTripName(), e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Failed to create trip group due to server error.");
            }
        }

        @GetMapping("/my")
        public ResponseEntity<List<TripGroup>> getUserGroups(@RequestParam String userId) {
            log.info("Fetching trip groups for user '{}'", userId);
            List<TripGroup> groups = tripGroupService.getUserGroups(userId);
            return ResponseEntity.ok(groups);
        }

    @DeleteMapping("/{tripGroupId}")
    public ResponseEntity<String> deleteGroup(@PathVariable String tripGroupId,@RequestParam String userId) {
        tripGroupService.deleteGroup(tripGroupId,userId);
        return ResponseEntity.ok("Trip group deleted successfully.");
    }

    @GetMapping("/{tripGroupId}")
    public ResponseEntity<?> getGroupById(@PathVariable String tripGroupId) {
        try {
            TripGroup group = tripGroupService.getGroupById(tripGroupId);
            return ResponseEntity.ok(group);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Trip group not found.");
        }
    }

//    @PreAuthorize("hasRole('USER')")
    @PostMapping("/{groupId}/members")
    public ResponseEntity<?> addMemberToGroup(@PathVariable String groupId, @RequestParam String userId) {
        try {
            tripGroupService.addMemberToGroup(groupId, userId);
            return ResponseEntity.ok("User added to group successfully");
        } catch (Exception e) {
            log.error("Error adding user to group", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add user to group");
        }
    }


}


