package com.mycompany.flightapp.service;

import com.mycompany.flightapp.model.TripNote;

import java.util.List;

public interface TripNoteService {
    TripNote addNote(String tripGroupId, String content, String createdBy);
    List<TripNote> getNotesForTripGroup(String tripGroupId);
    TripNote updateNote(String noteId, TripNote noteRequest);
    void deleteNote(String noteId);
}
