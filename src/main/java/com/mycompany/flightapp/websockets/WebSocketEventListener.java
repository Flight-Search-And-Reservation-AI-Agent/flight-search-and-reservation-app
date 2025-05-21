package com.mycompany.flightapp.websockets;

import com.mycompany.flightapp.service.PresenceService;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebSocketEventListener {

    private final PresenceService presenceService;

    public WebSocketEventListener(PresenceService presenceService) {
        this.presenceService = presenceService;
    }

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        // Not directly useful unless user sends a JOIN message
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String tripGroupId = (String) headerAccessor.getSessionAttributes().get("tripGroupId");
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        if (tripGroupId != null && username != null) {
            presenceService.userDisconnected(tripGroupId, username);
        }
    }
}
