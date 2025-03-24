//package com.mycompany.flightapp;
//
//import com.mycompany.flightapp.dto.FlightDTO;
//import com.mycompany.flightapp.model.Aircraft;
//import com.mycompany.flightapp.model.Airport;
//import com.mycompany.flightapp.model.Flight;
//import com.mycompany.flightapp.repository.AircraftRepository;
//import com.mycompany.flightapp.repository.AirportRepository;
//import com.mycompany.flightapp.repository.FlightRepository;
//import com.mycompany.flightapp.service.FlightService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@Transactional
//public class FlightServiceImplTest {
//    @Autowired
//    private FlightService flightService;
//    @Autowired
//    private FlightRepository flightRepository;
//    @Autowired
//    private AirportRepository airportRepository;
//    @Autowired
//    private AircraftRepository aircraftRepository;
//
//    private Airport origin;
//    private Airport destination;
//    private Aircraft aircraft;
//    private FlightDTO flightDTO;
//
//
//
//    @BeforeEach
//    void setUp(){
//        // Create airports
//        origin=new Airport("Meerut","MER","Meerut","India");
//        destination= new Airport("Noida","NOI","Noida","India");
//        origin=airportRepository.save(origin);
//        destination=airportRepository.save(destination);
//
//        //Create aircraft
//        aircraft=new Aircraft("Boeing 777",300);
//        aircraft = aircraftRepository.save(aircraft);
//        flightDTO =new FlightDTO();
//        flightDTO.setFlightNumber("BA101");
//        flightDTO.setDepartureTime(LocalDateTime.now().plusDays(1));
//        flightDTO.setArrivalTime(LocalDateTime.now().plusDays(1).plusHours(3));
//        flightDTO.setOriginAirportId(origin.getAirportId());
//        flightDTO.setDestinationAirportId(destination.getAirportId());
//        flightDTO.setAircraftId(aircraft.getAircraftId());
//        flightDTO.setPrice(3200.00);
//
//    }
//
//    @Test
//    void testAddFlight(){
//        Flight flight= flightService.addFlight(flightDTO);
//        assertNotNull(flight.getFlightId(),"Flight id should not be null after creation");
//        assertEquals("BA101",flight.getFlightNumber(),"Flight number should match");
//    }
//
//    @Test
//    void testSearchFlight(){
//        // Insert a flight that should match the search criteria
//        FlightDTO searchFlight = new FlightDTO();
//        searchFlight.setFlightNumber("AI204");
//        LocalDateTime departureTime = LocalDateTime.now().plusDays(2).withHour(10).withMinute(0);
//        searchFlight.setDepartureTime(departureTime);
//        searchFlight.setArrivalTime(departureTime.plusHours(3));
//        searchFlight.setOriginAirportId(origin.getAirportId());
//        searchFlight.setDestinationAirportId(destination.getAirportId());
//        searchFlight.setAircraftId(aircraft.getAircraftId());
//        searchFlight.setPrice(8000.00);
//        flightService.addFlight(searchFlight);
//
//        List<Flight> flights= flightService.searchFlights(origin.getCode(),destination.getCode(),departureTime);
//        assertFalse(flights.isEmpty(),"Search should return atleast one flight");
//    }
//
//    @Test
//    void testGetFlightById(){
//        Flight flight=flightService.getFlightById("999L");
//        assertNull(flight,"No flight should be found with non-existing ID");
//    }
//
//    @Test
//    void testUpdateFlight(){
//        FlightDTO flightDTO = new FlightDTO();
//        flightDTO.setFlightNumber("AI202-I");
//        LocalDateTime departureTime = LocalDateTime.now().plusDays(3).withHour(10).withMinute(0);
//        flightDTO.setDepartureTime(departureTime.plusHours(1));
//        flightDTO.setArrivalTime(departureTime.plusHours(3));
//        flightDTO.setOriginAirportId(origin.getAirportId());
//        flightDTO.setDestinationAirportId(destination.getAirportId());
//        flightDTO.setAircraftId(aircraft.getAircraftId());
//        flightDTO.setPrice(7500.00);
//        Flight flight = flightService.addFlight(flightDTO);
//
//        // Update the flight details
//        FlightDTO updateDTO = new FlightDTO();
//        updateDTO.setFlightNumber("AI204-UPD");
//        updateDTO.setDepartureTime(departureTime.plusHours(1));
//        updateDTO.setArrivalTime(departureTime.plusHours(3));
//        updateDTO.setOriginAirportId(origin.getAirportId());
//        updateDTO.setDestinationAirportId(destination.getAirportId());
//        updateDTO.setAircraftId(aircraft.getAircraftId());
//        updateDTO.setPrice(7500.00);
//
//        Flight updatedFlight=flightService.updateFlight(flight.getFlightId(),updateDTO);
//        assertEquals("AI204-UPD",updatedFlight.getFlightNumber(),"Flight number should be updated");
//        assertEquals(7500.00,updatedFlight.getPrice(),"FLight price should be updated");
//    }
//
//    @Test
//    void testDeleteFLight(){
//        Flight flight=flightService.addFlight(flightDTO);
//        boolean deleted = flightService.deleteFlight(flight.getFlightId());
//        assertTrue(deleted, "The flight should be deleted successfully");
//    }
//}
