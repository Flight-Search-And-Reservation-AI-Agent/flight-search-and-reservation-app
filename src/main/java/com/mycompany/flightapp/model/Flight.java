package com.mycompany.flightapp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "flights")
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long flightId;

    @Column(nullable = false, unique = true)
    private String flightNumber;

    @Column(nullable = false)
    private LocalDateTime departureTime;

    @Column(nullable = false)
    private LocalDateTime arrivalTime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "origin_id", nullable = false)
    private Airport origin;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "destination_id", nullable = false)
    private Airport destination;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "aircraft_id", nullable = false)
    private Aircraft aircraft;

    @Column(nullable = false)
    private Double price;
}
