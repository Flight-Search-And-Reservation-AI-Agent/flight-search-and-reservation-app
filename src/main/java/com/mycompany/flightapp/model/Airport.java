package com.mycompany.flightapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor  // Required for JPA
@AllArgsConstructor // Required for custom constructor
@Table(name = "airports")
public class Airport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long airportId;

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
