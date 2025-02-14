package com.mycompany.flightapp.repository;

import com.mycompany.flightapp.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, String> {
    @Query("SELECT r FROM Reservation r WHERE r.user.userId = :userId")
    List<Reservation> findReservationsByUserId(@Param("userId") String userId);

}

