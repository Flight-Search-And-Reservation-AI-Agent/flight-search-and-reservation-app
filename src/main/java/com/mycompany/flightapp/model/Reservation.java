package com.mycompany.flightapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;

    // For demonstration, assume the User and Flight entities exist.
    // In a production application, you would define these relationships fully.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flight_id", nullable = false)
    private Flight flight;

    private String seatNumber;

    @NotNull
    private String status; // e.g., "BOOKED" or "CANCELLED"

    private LocalDateTime reservationTime;

}

