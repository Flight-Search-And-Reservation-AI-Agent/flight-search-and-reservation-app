package com.mycompany.flightapp.controller;

import com.mycompany.flightapp.model.ChatMessage;
import com.mycompany.flightapp.repository.ChatMessageRepository;
import com.mycompany.flightapp.service.PresenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/v1/chat")
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageRepository chatMessageRepository;
    private final PresenceService presenceService;

    @Autowired
    public ChatController(SimpMessagingTemplate messagingTemplate, ChatMessageRepository chatMessageRepository, PresenceService presenceService) {
        this.messagingTemplate = messagingTemplate;
        this.chatMessageRepository = chatMessageRepository;
        this.presenceService = presenceService;
    }

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(ChatMessage chatMessage) {
        // 1. Save to DB
        ChatMessage entity = new ChatMessage(
                null,                             // id (UUID will be auto-generated)
                chatMessage.getTripGroupId(),
                chatMessage.getSenderUsername(),
                chatMessage.getContent(),
                Instant.now()                     // timestamp
        );
        ChatMessage savedMessage = chatMessageRepository.save(entity);

        // 2. Send to WebSocket subscribers
        messagingTemplate.convertAndSend(
                "/topic/group/" + savedMessage.getTripGroupId(),
                savedMessage
        );
    }

    // Fetch chat history
    @GetMapping("/history/{tripGroupId}")
    public List<ChatMessage> getChatHistory(@PathVariable String tripGroupId) {
        return chatMessageRepository.findByTripGroupIdOrderByTimestampAsc(tripGroupId);
    }


    @MessageMapping("/chat.addUser")
    public void addUser(@Payload ChatMessage chatMessage,
                        SimpMessageHeaderAccessor headerAccessor) {
        // Save username and tripGroupId in WebSocket session
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSenderUsername());
        headerAccessor.getSessionAttributes().put("tripGroupId", chatMessage.getTripGroupId());

        // Mark user online
        presenceService.userConnected(chatMessage.getTripGroupId(), chatMessage.getSenderUsername());

        // Optionally broadcast updated list
        messagingTemplate.convertAndSend(
                "/topic/group/" + chatMessage.getTripGroupId() + "/presence",
                presenceService.getOnlineUsers(chatMessage.getTripGroupId())
        );
    }
}
