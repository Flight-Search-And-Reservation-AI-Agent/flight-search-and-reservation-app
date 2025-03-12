//package com.mycompany.flightapp.config;
//
//import com.mycompany.flightapp.model.User;
//import com.mycompany.flightapp.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//@Component
//public class TestUserLoader implements CommandLineRunner {
//    @Autowired
//    private UserRepository userRepository;
//
//    @Override
//    public void run(String... args) {
//        // Create and save a test user (UUID will be auto-generated if not provided)
//        User testUser = new User();
//        testUser.setUsername("testuser");
//        testUser.setEmail("test@example.com");
//        testUser.setPassword("hashedpassword");
//        testUser.setRole("CUSTOMER");
//        // Save and print the generated UUID
//        User savedUser = userRepository.save(testUser);
//        System.out.println("Test user created with ID: " + savedUser.getUserId());
//    }
//}
