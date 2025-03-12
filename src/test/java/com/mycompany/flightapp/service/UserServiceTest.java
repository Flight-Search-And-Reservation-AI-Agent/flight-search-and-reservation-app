package com.mycompany.flightapp.service;

import com.mycompany.flightapp.dto.UserDTO;
import com.mycompany.flightapp.exception.ResourceNotFoundException;
import com.mycompany.flightapp.model.User;
import com.mycompany.flightapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserDTO userDTO;

    @BeforeEach
    void setUp(){
        // Prepare a sample User entity
        user = new User();
        user.setUserId("test-id");
        user.setUsername("testuser");
        user.setEmail("testuser@example.com");
        user.setPassword("hashedpassword");
        user.setRole("CUSTOMER");

        // Prepare a sample UserDTO for creation/updation
        userDTO = new UserDTO();
        userDTO.setUsername("testuser");
        userDTO.setEmail("testuser@example.com");
        userDTO.setPassword("password123");
        userDTO.setRole("CUSTOMER");
    }

    @Test
    void testCreateUser_Success(){
        //Stimulate the no user exist with given username
        when(userRepository.findByUsername(userDTO.getUsername())).thenReturn(Optional.empty());
        //Stimulate password encryption
        when(passwordEncoder.encode(userDTO.getPassword())).thenReturn("hashedpassword");
        //Stimulate saving the user
        when(userRepository.save(any(User.class))).thenReturn(user);

        User createdUser=userService.createUser(userDTO);
        assertNotNull(createdUser);
        assertEquals("testuser",createdUser.getUsername());
        verify(userRepository,times(1)).save(any(User.class));
    }

    @Test
    void testCreateUser_UserExists(){
        // Simulate that a user already exists with the given username
        when(userRepository.findByUsername(userDTO.getUsername())).thenReturn(Optional.of(user));
        Exception exception= assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser(userDTO);
        });
        String expectedMessage = "Username already taken";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testGetUserById_Success(){
        when(userRepository.findByUserId("test-id")).thenReturn(Optional.of(user));
        Optional<User> foundUser = userService.getUserById("test-id");
        assertTrue(foundUser.isPresent());
        assertEquals("testuser",foundUser.get().getUsername());
    }

    @Test
    void testGetUserById_NotFound(){
        when(userRepository.findByUserId("wrong-id")).thenReturn(Optional.empty());
        Optional<User> foundUser = userService.getUserById("wrong-id");
        assertFalse(foundUser.isPresent());
    }

    @Test
    void testUpdateUser_Success(){
        // Simulate an existing user with ID "test-id"
        when(userRepository.findByUserId("test-id")).thenReturn(Optional.of(user));
        //Stimulate password encryption
        when(passwordEncoder.encode(userDTO.getPassword())).thenReturn("newhashedpassword");
        //Stimulate saving the user
        when(userRepository.save(any(User.class))).thenReturn(user);

        User updatedUser = userService.updateUser("test-id", userDTO);
        assertNotNull(updatedUser);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testUpdateUser_NotFound(){
        when(userRepository.findByUserId("wrong-id")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> userService.updateUser("wrong-id", userDTO));
    }

    @Test
    void testDeleteUser_Success(){
        // Simulate an existing user with ID "test-id"
        when(userRepository.findByUserId("test-id")).thenReturn(Optional.of(user));
        doNothing().when(userRepository).delete(user);
        assertDoesNotThrow(()-> userService.deleteUser("test-id"));
        verify(userRepository,times(1)).delete(user);
    }

    @Test
    void testDeleteUser_NotFound() {
        boolean result = userService.deleteUser("wrong-id");

        assertFalse(result);
        verify(userRepository, never()).delete(any(User.class));
    }
}
