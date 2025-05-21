package com.mycompany.flightapp.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "trip_groups")
public class TripGroup {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "trip_group_id", updatable = false, nullable = false)
    private String tripGroupId;

    @Column(unique = true)
    private String tripName;

    private String tripDescription;

    private String status;

    private String tripAvatarUrl;

    private String dates;

    // Trip Created by
    @ManyToOne
    private User createdBy;

    // Trip Members
    @ManyToMany
    private List<User> members = new ArrayList<>();

    // ---------------- NEW Dashboard fields ----------------

    // Relationships to Polls (polls will have their own entity)
    @OneToMany(mappedBy = "tripGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Poll> polls = new ArrayList<>();

    @OneToMany(mappedBy = "tripGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<TripNote> tripNotes = new ArrayList<>();

    @OneToMany(mappedBy = "tripGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ChecklistItem> checklistItems = new ArrayList<>();

}
