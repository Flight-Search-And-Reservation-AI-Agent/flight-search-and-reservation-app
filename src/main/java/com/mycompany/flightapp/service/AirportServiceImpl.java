package com.mycompany.flightapp.service;

import com.mycompany.flightapp.exception.ResourceNotFoundException;
import com.mycompany.flightapp.model.Airport;
import com.mycompany.flightapp.repository.AirportRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class AirportServiceImpl implements AirportService {
    private final AirportRepository airportRepository;

    @Autowired
    public AirportServiceImpl(AirportRepository airportRepository) {
        this.airportRepository = airportRepository;
    }

    @Override
    public List<Airport> getAllAirports(){
        return airportRepository.findAll();
    }

    @Override
    public Airport createAirport(Airport airport){
        log.info("Creating new airport with code: {}",airport.getCode());
        return airportRepository.save(airport);
    }

    @Override
    public Airport updateAirport(String airportId, Airport airportDetails) {
        Airport existingAirport = airportRepository.findById(airportId)
                .orElseThrow(() -> new ResourceNotFoundException("Airport not found with id: " + airportId));
        existingAirport.setCode(airportDetails.getCode());
        existingAirport.setName(airportDetails.getName());
        existingAirport.setCity(airportDetails.getCity());
        existingAirport.setCountry(airportDetails.getCountry());
        log.info("Updating airport with id: {}", airportId);
        return airportRepository.save(existingAirport);
    }

    @Override
    public Optional<Airport> getAirportById(String airportId){
        return airportRepository.findById(airportId);
    }

    @Override
    public boolean deleteAirport(String airportId){
        Optional<Airport> airportOpt= airportRepository.findById(airportId);
        if(!airportOpt.isPresent()){
            log.warn("Airport not found for deletion with id: {}",airportId);
            return false;
        }
        airportRepository.delete(airportOpt.get());
        log.info("Delete airport with id: {}",airportId);
        return true;
    }
}
