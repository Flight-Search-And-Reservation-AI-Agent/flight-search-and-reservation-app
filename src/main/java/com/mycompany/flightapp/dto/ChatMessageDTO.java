package com.mycompany.flightapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDTO {
    private String senderUsername;
    private String content;
    private String timestamp; // ISO String (optional)
    private Long tripGroupId;
}
