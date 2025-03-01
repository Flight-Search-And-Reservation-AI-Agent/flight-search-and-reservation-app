package com.mycompany.flightapp.service;

import com.mycompany.flightapp.exception.ResourceNotFoundException;
import com.mycompany.flightapp.model.Aircraft;
import com.mycompany.flightapp.repository.AircraftRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class AircraftServiceImpl implements AircraftService {
    private final AircraftRepository aircraftRepository;

    @Autowired
    public AircraftServiceImpl(AircraftRepository aircraftRepository) {
        this.aircraftRepository = aircraftRepository;
    }

    @Override
    public List<Aircraft> getAllAircrafts(){
        return aircraftRepository.findAll();
    }

    @Override
    public Optional<Aircraft> getAircraftWithId(String aircraftId){
        return aircraftRepository.findById(aircraftId);
    }

    @Override
    public Aircraft createAircraft(Aircraft aircraft){
        log.info("Create new aircraft with model: {}",aircraft.getModel());
        return aircraftRepository.save(aircraft);
    }

    @Override
    public Aircraft updateAircraft(String aircraftId,Aircraft aircraft){
        Aircraft existingAircraft= aircraftRepository.findById(aircraftId).orElseThrow(() -> new ResourceNotFoundException("Aircraft not found with id: "+aircraftId));
        existingAircraft.setModel(aircraft.getModel());
        existingAircraft.setCapacity(aircraft.getCapacity());
        log.info("Updating aircraft with id: {}",aircraftId);
        return aircraftRepository.save(existingAircraft);
    }

    @Override
    public boolean deleteAircraft(String aircraftId){
        Optional<Aircraft> aircraftOpt = aircraftRepository.findById(aircraftId);
        if (!aircraftOpt.isPresent()){
            log.warn("Aircraft not found for deletion with id: {}", aircraftId);
            return false;
        }
        aircraftRepository.delete(aircraftOpt.get());
        log.info("Delete aircraft with id: {}",aircraftId);
        return true;
    }
}
