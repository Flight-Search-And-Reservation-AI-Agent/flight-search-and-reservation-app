package com.mycompany.flightapp.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
@Slf4j
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        log.info("WebSocket request received: " + request.getURI());
        String token = request.getHeaders().getFirst("Authorization");
        log.info("token: " + token);
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);  // Remove "Bearer " part
            if (validateJwtToken(token)) {
                // Store the user or relevant information from the token
                attributes.put("user", "extractedUserFromToken");
                return true;  // Allow WebSocket handshake
            } else {
                response.setStatusCode(HttpStatus.FORBIDDEN);  // 403 if token is invalid
                return false;  // Reject handshake
            }
        } else {
            response.setStatusCode(HttpStatus.FORBIDDEN);  // 403 if token is missing
            return false;  // Reject handshake
        }
    }

    private boolean validateJwtToken(String token) {
        try {
            // Implement JWT validation logic (e.g., using JWT library to parse and verify)
            // For example, you could decode and verify the JWT signature here
            return true;  // Replace with actual validation
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception ex) {
        // You can add any post-handshake logic here, if necessary
    }


}
