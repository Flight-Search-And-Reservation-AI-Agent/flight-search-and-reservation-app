package com.mycompany.flightapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TripGroupDTO {
    private String tripGroupId;
    private String tripName;
    private String tripDescription;
    private String status;
    private String tripAvatarUrl;
    private String dates;
    private List<String> invitedUserIds;
}

