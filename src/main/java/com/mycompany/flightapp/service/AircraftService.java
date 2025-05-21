package com.mycompany.flightapp.service;

import com.mycompany.flightapp.model.Aircraft;

import java.util.List;
import java.util.Optional;

public interface AircraftService {
    List<Aircraft> getAllAircrafts();

    Optional<Aircraft> getAircraftWithId(String aircraftId);

    Aircraft createAircraft(Aircraft aircraft);

    Aircraft updateAircraft(String aircraftId,Aircraft aircraft);

    boolean deleteAircraft(String aircraftId);
}
