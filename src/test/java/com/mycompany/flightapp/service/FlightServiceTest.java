//package com.mycompany.flightapp.service;
//
//import com.mycompany.flightapp.dto.FlightDTO;
//import com.mycompany.flightapp.exception.ResourceNotFoundException;
//import com.mycompany.flightapp.model.Aircraft;
//import com.mycompany.flightapp.model.Airport;
//import com.mycompany.flightapp.model.Flight;
//import com.mycompany.flightapp.repository.AircraftRepository;
//import com.mycompany.flightapp.repository.AirportRepository;
//import com.mycompany.flightapp.repository.FlightRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class FlightServiceTest {
//
//    @Mock
//    private FlightRepository flightRepository;
//
//    @Mock
//    private AirportRepository airportRepository;
//
//    @Mock
//    private AircraftRepository aircraftRepository;
//
//    @InjectMocks
//    private FlightServiceImpl flightService;
//
//    private Flight flight;
//    private FlightDTO flightDTO;
//    private Airport originAirport;
//    private Airport destinationAirport;
//    private Aircraft aircraft;
//
//    @BeforeEach
//    void setUp() {
//        originAirport = new Airport("Delhi", "DEL", "Delhi", "India");
//        destinationAirport = new Airport("Mumbai", "BOM", "Mumbai", "India");
//        aircraft = new Aircraft("Boeing 777", 300);
//
//        flight = new Flight();
//        flight.setFlightId("1L");
//        flight.setFlightNumber("AI101");
//        flight.setDepartureTime(LocalDateTime.now().plusDays(1));
//        flight.setArrivalTime(LocalDateTime.now().plusDays(1).plusHours(2));
//        flight.setOrigin(originAirport);
//        flight.setDestination(destinationAirport);
//        flight.setAircraft(aircraft);
//        flight.setPrice(5000.0);
//
//        flightDTO = new FlightDTO("AI101",
//                flight.getDepartureTime(),
//                flight.getArrivalTime(),
//                "1L", "2L", "1L", 5000.0);
//    }
//
//    @Test
//    void testAddFlight_Success() {
//        when(airportRepository.findById("1L")).thenReturn(Optional.of(originAirport));
//        when(airportRepository.findById("2L")).thenReturn(Optional.of(destinationAirport));
//        when(aircraftRepository.findById("1L")).thenReturn(Optional.of(aircraft));
//        when(flightRepository.save(any(Flight.class))).thenReturn(flight);
//
//        Flight createdFlight = flightService.addFlight(flightDTO);
//
//        assertNotNull(createdFlight);
//        assertEquals("AI101", createdFlight.getFlightNumber());
//        verify(flightRepository, times(1)).save(any(Flight.class));
//    }
//
//    @Test
//    void testAddFlight_OriginNotFound() {
//        when(airportRepository.findById("1L")).thenReturn(Optional.empty());
//
//        Exception exception = assertThrows(ResourceNotFoundException.class, () -> flightService.addFlight(flightDTO));
//        assertEquals("Origin Airport not found", exception.getMessage());
//    }
//
//    @Test
//    void testAddFlight_DestinationNotFound() {
//        when(airportRepository.findById("1L")).thenReturn(Optional.of(originAirport));
//        when(airportRepository.findById("2L")).thenReturn(Optional.empty());
//
//        Exception exception = assertThrows(ResourceNotFoundException.class, () -> flightService.addFlight(flightDTO));
//        assertEquals("Destination Airport not found", exception.getMessage());
//    }
//
//    @Test
//    void testAddFlight_AircraftNotFound() {
//        when(airportRepository.findById("1L")).thenReturn(Optional.of(originAirport));
//        when(airportRepository.findById("2L")).thenReturn(Optional.of(destinationAirport));
//        when(aircraftRepository.findById("1L")).thenReturn(Optional.empty());
//
//        Exception exception = assertThrows(ResourceNotFoundException.class, () -> flightService.addFlight(flightDTO));
//        assertEquals("Aircraft not found", exception.getMessage());
//    }
//
//    @Test
//    void testFindFlightsByOriginDestination() {
//        when(flightRepository.findByOrigin_CodeAndDestination_CodeAndDepartureTimeBetween(
//                eq("DEL"), eq("BOM"), any(LocalDateTime.class), any(LocalDateTime.class)
//        )).thenReturn(List.of(flight));
//
//
//        List<Flight> flights = flightService.searchFlights("DEL", "BOM", flight.getDepartureTime());
//
//        assertFalse(flights.isEmpty());
//        assertEquals(1, flights.size());
//        verify(flightRepository, times(1))
//                .findByOrigin_CodeAndDestination_CodeAndDepartureTimeBetween(anyString(), anyString(), any(), any());
//    }
//
//    @Test
//    void testDeleteFlight_Success() {
//        when(flightRepository.findById("1L")).thenReturn(Optional.of(flight));
//        doNothing().when(flightRepository).delete(flight);
//
//        boolean deleted = flightService.deleteFlight("1L");
//
//        assertTrue(deleted);
//        verify(flightRepository, times(1)).delete(flight);
//    }
//
//    @Test
//    void testDeleteFlight_NotFound() {
//        when(flightRepository.findById("1L")).thenReturn(Optional.empty());
//
//        boolean deleted = flightService.deleteFlight("1L");
//
//        assertFalse(deleted);
//        verify(flightRepository, never()).delete(any(Flight.class));
//    }
//}
