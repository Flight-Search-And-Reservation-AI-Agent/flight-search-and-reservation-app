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
public class TripNote {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private String noteId;

    private String content;

    private String createdBy; // username or userId

    @ManyToOne
    @JoinColumn(name = "trip_group_id")
    @JsonBackReference
    private TripGroup tripGroup;
}
