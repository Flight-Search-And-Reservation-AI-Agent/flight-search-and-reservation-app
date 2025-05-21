package com.mycompany.flightapp.service;

import com.mycompany.flightapp.dto.CheckListDTO;
import com.mycompany.flightapp.model.ChecklistItem;

import java.util.List;

public interface ChecklistService {
    ChecklistItem addItem(String tripGroupId, String task, String assignedTo);
    List<ChecklistItem> getChecklistForTripGroup(String tripGroupId);
    ChecklistItem toggleComplete(String itemId);
    ChecklistItem updateChecklist(String checklistId, CheckListDTO checklistItem);
    void deleteChecklist(String checklistId);
}
