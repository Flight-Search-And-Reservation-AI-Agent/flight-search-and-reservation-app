package com.mycompany.flightapp.service;

import com.mycompany.flightapp.model.Airport;
import com.mycompany.flightapp.repository.AirportRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AirportServiceTest {

    @Mock
    private AirportRepository airportRepository;

    private Airport airport;

    @BeforeEach
    void setUp(){
        // Create airport object
        airport = new Airport();
        airport.setAirportId("1L");
        airport.setCode("DEL");
        airport.setName("Indira Gandhi International Airport");
        airport.setCity("Delhi");
        airport.setCountry("India");
    }

    @Test
    void testSaveAirport(){
        when(airportRepository.save(any(Airport.class))).thenReturn(airport);
        Airport savedAirport =airportRepository.save(airport);
        assertNotNull(savedAirport);
        assertEquals("DEL", savedAirport.getCode());
        verify(airportRepository, times(1)).save(any(Airport.class));
    }

    @Test
    void testFindById_Success(){
        when(airportRepository.findById("1L")).thenReturn(Optional.of(airport));
        Optional<Airport> foundAirport = airportRepository.findById("1L");

        assertTrue(foundAirport.isPresent());
        assertEquals("Delhi",foundAirport.get().getCity());
        verify(airportRepository,times(1)).findById("1L");
    }

    @Test
    void testFindById_NotFound(){
        when(airportRepository.findById("invalidId")).thenReturn(Optional.empty());
        Optional<Airport> foundAirport = airportRepository.findById("invalidId");

        assertFalse(foundAirport.isPresent());
        verify(airportRepository,times(1)).findById("invalidId");
    }

    @Test
    void testFindAll(){
        List<Airport> airportList = Arrays.asList(airport, new Airport("2L", "BOM", "Chhatrapati Shivaji International Airport", "Mumbai", "India"));
        when(airportRepository.findAll()).thenReturn(airportList);

        List<Airport> foundAirports = airportRepository.findAll();

        assertEquals(2,foundAirports.size());
        verify(airportRepository,times(1)).findAll();
    }

    @Test
    void testDeleteAirport(){
        doNothing().when(airportRepository).delete(airport);
        airportRepository.delete(airport);
        verify(airportRepository,times(1)).delete(airport);
    }
}
