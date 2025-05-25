package com.mycompany.flightapp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserDTO {

    private String username;

    @Email(message = "Invalid email format")
    private String email;


    private String password;

//    @NotBlank(message = "Role is required")
    private String role;
}
