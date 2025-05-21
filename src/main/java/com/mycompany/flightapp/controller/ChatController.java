package com.mycompany.flightapp.controller;

import com.mycompany.flightapp.model.ChatMessage;
import com.mycompany.flightapp.repository.ChatMessageRepository;
import com.mycompany.flightapp.service.PresenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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

    @MessageMapping("/sendMessage")
    public void sendMessage(ChatMessage chatMessage) {
        chatMessage.setTimestamp(LocalDateTime.now());
        // 1. Save to DB
        ChatMessage savedMessage = chatMessageRepository.save(chatMessage);

        // 2. Send to WebSocket subscribers
        messagingTemplate.convertAndSend(
                "/topic/group/" + savedMessage.getGroupId(),
                savedMessage
        );
    }

    // Fetch chat history
    @GetMapping("/messages/{groupId}")
    public ResponseEntity<List<ChatMessage>> getMessages(@PathVariable String groupId) {
        return ResponseEntity.ok(chatMessageRepository.findByGroupIdOrderByTimestampAsc(groupId));
    }




}
