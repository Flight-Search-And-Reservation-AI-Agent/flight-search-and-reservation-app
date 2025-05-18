package com.mycompany.flightapp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChecklistItem {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private String itemId;

    private String task;

    private boolean completed = false;

    private String assignedTo; // username or userId

    @ManyToOne
    @JoinColumn(name = "trip_group_id")
    @JsonBackReference
    private TripGroup tripGroup;
}
