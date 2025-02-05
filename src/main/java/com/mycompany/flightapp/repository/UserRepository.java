package com.mycompany.flightapp.repository;

import com.mycompany.flightapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Custom finder methods (if needed)
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
}
