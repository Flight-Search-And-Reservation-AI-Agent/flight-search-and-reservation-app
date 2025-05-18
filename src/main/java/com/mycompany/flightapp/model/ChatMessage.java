package com.mycompany.flightapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.time.Instant;

@Entity
@Table(name = "chat_messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "chat_message_id", updatable = false, nullable = false)
    private String id;

    private String tripGroupId;

    private String senderUsername;

    @Column(columnDefinition = "TEXT")
    private String content;

    private Instant timestamp; // store as Instant for consistency
}

