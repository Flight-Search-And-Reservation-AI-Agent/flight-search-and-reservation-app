package com.mycompany.flightapp.service;

import com.mycompany.flightapp.dto.CheckListDTO;
import com.mycompany.flightapp.model.ChecklistItem;
import com.mycompany.flightapp.model.TripGroup;
import com.mycompany.flightapp.repository.ChecklistItemRepository;
import com.mycompany.flightapp.repository.TripGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChecklistServiceImpl implements ChecklistService {

    @Autowired
    private ChecklistItemRepository checklistItemRepository;

    @Autowired
    private TripGroupRepository tripGroupRepository;

    @Override
    public ChecklistItem addItem(String tripGroupId, String task, String assignedTo) {
        TripGroup tripGroup = tripGroupRepository.findById(tripGroupId)
                .orElseThrow(() -> new RuntimeException("Trip group not found"));

        ChecklistItem item = ChecklistItem.builder()
                .task(task)
                .assignedTo(assignedTo)
                .completed(false)
                .tripGroup(tripGroup)
                .build();

        return checklistItemRepository.save(item);
    }

    @Override
    public List<ChecklistItem> getChecklistForTripGroup(String tripGroupId) {
        return checklistItemRepository.findByTripGroup_TripGroupId(tripGroupId);
    }

    @Override
    public ChecklistItem toggleComplete(String itemId) {
        ChecklistItem item = checklistItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));
        item.setCompleted(!item.isCompleted());
        return checklistItemRepository.save(item);
    }

    @Override
    public ChecklistItem updateChecklist(String checklistId, CheckListDTO checklistItem) {
        ChecklistItem existing = checklistItemRepository.findById(checklistId)
                .orElseThrow(() -> new RuntimeException("Checklist item not found"));
        existing.setTask(checklistItem.getTask());
        existing.setAssignedTo(checklistItem.getAssignedTo());
        return checklistItemRepository.save(existing);
    }

    @Override
    public void deleteChecklist(String checklistId) {
        checklistItemRepository.deleteById(checklistId);
    }

}
