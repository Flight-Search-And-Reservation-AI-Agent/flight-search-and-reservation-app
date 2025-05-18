package com.mycompany.flightapp.repository;

import com.mycompany.flightapp.model.ChecklistItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ChecklistItemRepository extends JpaRepository<ChecklistItem, String> {
    List<ChecklistItem> findByTripGroup_TripGroupId(String tripGroupId);
}
