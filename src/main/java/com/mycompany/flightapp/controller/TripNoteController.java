package com.mycompany.flightapp.controller;

import com.mycompany.flightapp.dto.TripNoteDTO;
import com.mycompany.flightapp.model.TripNote;
import com.mycompany.flightapp.service.TripNoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/trip-groups/{tripGroupId}/notes")
public class TripNoteController {

    @Autowired
    private TripNoteService tripNoteService;

    @PostMapping
    public ResponseEntity<TripNote> addNote(
            @PathVariable String tripGroupId,
            @RequestBody TripNoteDTO tripNoteDTO) {

        TripNote note = tripNoteService.addNote(tripGroupId, tripNoteDTO.getContent(),tripNoteDTO.getCreatedBy());
        return ResponseEntity.ok(note);
    }

    @GetMapping
    public ResponseEntity<List<TripNote>> getNotes(@PathVariable String tripGroupId) {
        List<TripNote> notes = tripNoteService.getNotesForTripGroup(tripGroupId);
        return ResponseEntity.ok(notes);
    }

    @PutMapping("/{noteId}")
    public ResponseEntity<TripNote> updateNote(@PathVariable String noteId,
                                               @RequestBody TripNote noteRequest) {
        TripNote updated = tripNoteService.updateNote(noteId, noteRequest);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{noteId}")
    public ResponseEntity<String> deleteNote(@PathVariable String noteId) {
        tripNoteService.deleteNote(noteId);
        return ResponseEntity.ok("Note deleted successfully.");
    }

}

