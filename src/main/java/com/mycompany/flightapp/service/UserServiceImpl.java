package com.mycompany.flightapp.service;

import com.mycompany.flightapp.dto.UserDTO;
import com.mycompany.flightapp.exception.ResourceNotFoundException;
import com.mycompany.flightapp.model.User;
import com.mycompany.flightapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(UserDTO userDTO) {
        // In production, ensure that password is hashed, and you handle duplicate emails/usernames
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword()); // Hash the password in a real application!
        user.setRole(userDTO.getRole());
        return userRepository.save(user);
    }

    @Override
    public Optional<User> getUserById(String userId) {
        return userRepository.findByUserId(userId);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User updateUser(String userId, UserDTO userDTO) {
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword()); // Again, consider hashing the password
        user.setRole(userDTO.getRole());

        return userRepository.save(user);
    }

    @Override
    public void deleteUser(String userId) {
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        userRepository.delete(user);
    }
}
