package com.mycompany.flightapp.service;

import com.mycompany.flightapp.dto.UserDTO;
import com.mycompany.flightapp.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    // Create a new user
    User createUser(UserDTO userDTO);

    // Retrieve a user by ID
    Optional<User> getUserById(Long userId);

    // Retrieve all users
    List<User> getAllUsers();

    // Update user details
    User updateUser(Long userId, UserDTO userDTO);

    // Delete a user
    void deleteUser(Long userId);
}
