package com.mycompany.flightapp.service;

import com.mycompany.flightapp.model.TripGroup;
import com.mycompany.flightapp.model.TripNote;
import com.mycompany.flightapp.repository.TripGroupRepository;
import com.mycompany.flightapp.repository.TripNoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TripNoteServiceImpl implements TripNoteService {

    @Autowired
    private TripNoteRepository tripNoteRepository;

    @Autowired
    private TripGroupRepository tripGroupRepository;

    @Override
    public TripNote addNote(String tripGroupId, String content, String createdBy) {
        TripGroup tripGroup = tripGroupRepository.findById(tripGroupId)
                .orElseThrow(() -> new RuntimeException("Trip group not found"));

        TripNote note = TripNote.builder()
                .content(content)
                .createdBy(createdBy)
                .tripGroup(tripGroup)
                .build();

        return tripNoteRepository.save(note);
    }

    @Override
    public List<TripNote> getNotesForTripGroup(String tripGroupId) {
        return tripNoteRepository.findByTripGroup_TripGroupId(tripGroupId);
    }

    @Override
    public TripNote updateNote(String noteId, TripNote noteRequest) {
        TripNote existing = tripNoteRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Note not found"));
        existing.setContent(noteRequest.getContent());
        return tripNoteRepository.save(existing);
    }

    @Override
    public void deleteNote(String noteId) {
        tripNoteRepository.deleteById(noteId);
    }
}
