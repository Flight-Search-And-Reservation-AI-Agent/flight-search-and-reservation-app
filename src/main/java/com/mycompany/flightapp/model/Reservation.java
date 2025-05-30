package com.mycompany.flightapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "reservations")
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private String reservationId;

    // For demonstration, assume the User and Flight entities exist.
    // In a production application, you would define these relationships fully.
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "flight_id", nullable = false)
    private Flight flight;

    private String seatNumber;

    @NotNull
    private String status; // e.g., "BOOKED" or "CANCELLED"

    private LocalDateTime reservationTime;

    @ElementCollection
    @CollectionTable(name = "reservation_passengers", joinColumns = @JoinColumn(name = "reservation_id"))
    private List<Passenger> passengers;

}

