package com.axity.dinosaurpark.simulation;

import com.axity.dinosaurpark.model.Dinosaur;
import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.model.Vehicle;
import com.axity.dinosaurpark.persistence.DatabaseService;
import com.axity.dinosaurpark.zone.PowerPlant;
import java.util.ArrayList;
import java.util.List;

public class ParkState {
    // Cascarón temporal para que los eventos compilen
    public DatabaseService getDb() { return null; }
    public PowerPlant getPowerPlant() { return null; }
    public List<Dinosaur> getDinosaurs() { return new ArrayList<>(); }
    public List<Tourist> getActiveTourists() { return new ArrayList<>(); }
    public List<Vehicle> getVehicles() { return new ArrayList<>(); }
    public void setDealsHourActive(boolean active) {}
    public void setCurrentDiscount(double discount) {}
    public long getCurrentStep() { return 0; }
}