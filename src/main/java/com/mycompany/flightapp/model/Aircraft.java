package com.mycompany.flightapp.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Data
@NoArgsConstructor  // Required for JPA
@AllArgsConstructor // Allows object creation with parameters
@Table(name = "aircrafts")
public class Aircraft {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private String aircraftId;

    private String model;
    private int capacity;

    // Custom constructor (without ID)
    public Aircraft(String model, int capacity) {
        this.model = model;
        this.capacity = capacity;
    }
}

