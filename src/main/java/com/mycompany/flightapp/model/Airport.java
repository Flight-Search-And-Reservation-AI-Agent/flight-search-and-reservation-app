package com.mycompany.flightapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Data
@NoArgsConstructor  // Required for JPA
@AllArgsConstructor // Required for custom constructor
@Table(name = "airports")
public class Airport {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private String airportId;

    @NotBlank
    @Column(unique = true)
    private String code;

    @NotBlank
    private String name;

    private String city;
    private String country;

    // Custom constructor (without ID)
    public Airport(String name, String code, String city, String country) {
        this.name = name;
        this.code = code;
        this.city = city;
        this.country = country;
    }

}
