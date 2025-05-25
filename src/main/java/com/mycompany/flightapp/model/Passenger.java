package com.mycompany.flightapp.model;

import jakarta.persistence.*;
import lombok.*;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Passenger {
    private String name;
    private int age;
    private String gender;
}