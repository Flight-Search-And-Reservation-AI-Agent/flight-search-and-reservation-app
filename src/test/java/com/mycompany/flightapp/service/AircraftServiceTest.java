package com.mycompany.flightapp.service;

import com.mycompany.flightapp.model.Aircraft;
import com.mycompany.flightapp.repository.AircraftRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AircraftServiceTest {
    @Mock
    private AircraftRepository aircraftRepository;

    @InjectMocks
    private AircraftServiceImpl aircraftService;

    private Aircraft aircraft;

    @BeforeEach
    void setUp(){
        // Assuming the 4th argument is something like airlineName or manufacturer
        aircraft = new Aircraft("A123", "Boeing 737", 200, "Air India");

    }

    @Test
    void testAddAircraft_Success(){
        when(aircraftRepository.save(any(Aircraft.class))).thenReturn(aircraft);
        Aircraft savedAircraft = aircraftService.createAircraft(aircraft);
        assertNotNull(savedAircraft);
        assertEquals("A123",savedAircraft.getAircraftId());
        verify(aircraftRepository,times(1)).save(any(Aircraft.class));
    }

    @Test
    void testGetAircraftById_Success(){
        when(aircraftRepository.findById("A123")).thenReturn(Optional.of(aircraft));
        Optional<Aircraft> foundAircraft = aircraftService.getAircraftWithId("A123");

        assertNotNull(foundAircraft.isPresent());
        assertEquals("A123",foundAircraft.get().getAircraftId());
        verify(aircraftRepository,times(1)).findById("A123");
    }

    @Test
    void testGetAircraftById_NotFound(){
        when(aircraftRepository.findById("abc")).thenReturn(Optional.empty());
        Optional<Aircraft> foundAircraft = aircraftService.getAircraftWithId("abc");

        assertFalse(foundAircraft.isPresent());
        verify(aircraftRepository,times(1)).findById("abc");
    }
}
