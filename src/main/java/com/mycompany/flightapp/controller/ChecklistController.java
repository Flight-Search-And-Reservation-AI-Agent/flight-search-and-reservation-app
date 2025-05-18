package com.mycompany.flightapp.controller;

import com.mycompany.flightapp.dto.CheckListDTO;
import com.mycompany.flightapp.model.ChecklistItem;
import com.mycompany.flightapp.service.ChecklistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/trip-groups/{tripGroupId}/checklist")
public class ChecklistController {

    @Autowired
    private ChecklistService checklistService;

    @PostMapping
    public ResponseEntity<ChecklistItem> addItem(
            @PathVariable String tripGroupId,
            @RequestBody CheckListDTO checkListDTO) {

        ChecklistItem item = checklistService.addItem(tripGroupId, checkListDTO.getTask(), checkListDTO.getAssignedTo());
        return ResponseEntity.ok(item);
    }

    @GetMapping
    public ResponseEntity<List<ChecklistItem>> getChecklist(@PathVariable String tripGroupId) {
        List<ChecklistItem> items = checklistService.getChecklistForTripGroup(tripGroupId);
        return ResponseEntity.ok(items);
    }

    @PatchMapping("/toggle/{itemId}")
    public ResponseEntity<ChecklistItem> toggleComplete(@PathVariable String itemId) {
        ChecklistItem updatedItem = checklistService.toggleComplete(itemId);
        return ResponseEntity.ok(updatedItem);
    }

    @PutMapping("/{checklistId}")
    public ResponseEntity<ChecklistItem> updateChecklist(@PathVariable String checklistId,
                                                         @RequestBody CheckListDTO checkListDTO) {
        ChecklistItem updated = checklistService.updateChecklist(checklistId, checkListDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{checklistId}")
    public ResponseEntity<String> deleteChecklist(@PathVariable String checklistId) {
        checklistService.deleteChecklist(checklistId);
        return ResponseEntity.ok("Checklist item deleted successfully.");
    }

}
