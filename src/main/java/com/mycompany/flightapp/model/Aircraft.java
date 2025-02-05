package com.mycompany.flightapp.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor  // Required for JPA
@AllArgsConstructor // Allows object creation with parameters
@Table(name = "aircrafts")
public class Aircraft {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long aircraftId;

    private String model;
    private int capacity;

    // Custom constructor (without ID)
    public Aircraft(String model, int capacity) {
        this.model = model;
        this.capacity = capacity;
    }
}

