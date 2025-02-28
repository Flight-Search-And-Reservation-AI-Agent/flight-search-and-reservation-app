package com.mycompany.flightapp.controller;

import com.mycompany.flightapp.dto.UserDTO;
import com.mycompany.flightapp.model.User;
import com.mycompany.flightapp.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
@Slf4j
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Create a new user
    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDTO userDTO) {
        try{
            User createdUser = userService.createUser(userDTO);
            return ResponseEntity.ok(createdUser);
        }catch (IllegalArgumentException e) {
            log.warn("Failed to create user: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        catch (Exception e){
            log.warn("Error creating user",e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating user");
        }

    }

    // Get user by ID
    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable String userId) {
        try{
            Optional<User> optionalUser=userService.getUserById(userId);
            if (optionalUser.isPresent()){
                return ResponseEntity.ok(optionalUser.get());
            }else{
                log.warn("User not found with id: {}",userId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with id:" + userId);
            }
        }
        catch (Exception e){
            log.warn("Error retrieving user with id: {}",userId,e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving user");
        }
    }

    // Get all users
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        try{
            List<User> users = userService.getAllUsers();
            return ResponseEntity.ok(users);
        }catch (Exception e){
            log.warn("Error retrieving user",e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);

        }
    }

    // Update a user
    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable String userId, @Valid @RequestBody UserDTO userDTO) {
        try{
            User updatedUser = userService.updateUser(userId, userDTO);
            if(updatedUser != null){
                return ResponseEntity.ok(updatedUser);
            }
            else {
                log.warn("User not found for update with id: {}",userId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with id:" + userId);
            }
        }
        catch (Exception e){
            log.warn("Error updating user with id: {}",userId,e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating user");
        }

    }

    // Delete a user
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable String userId) {
        try{
            boolean deleted = userService.deleteUser(userId);
            if (deleted) {
                return ResponseEntity.noContent().build();
            }else{
                log.warn("User not found to delete with id: {}",userId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with id:" + userId);
            }
        }
        catch (Exception e){
            log.warn("Error updating user with id: {}",userId,e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating user");
        }
    }
}
