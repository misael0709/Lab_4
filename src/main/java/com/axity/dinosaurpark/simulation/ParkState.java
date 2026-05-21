package com.axity.dinosaurpark.simulation;

import com.axity.dinosaurpark.model.Dinosaur;
import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.model.Vehicle;
import com.axity.dinosaurpark.model.VehicleStatus;
import com.axity.dinosaurpark.persistence.DatabaseService;
import com.axity.dinosaurpark.zone.PowerPlant;

import java.util.ArrayList;
import java.util.List;

public class ParkState {
    private final DatabaseService db;
    private final PowerPlant powerPlant;
    private final List<Dinosaur> dinosaurs;
    private final List<Tourist> activeTourists;
    private final List<Vehicle> vehicles;
    
    private long currentStep;
    private boolean dealsHourActive;
    private double currentDiscount;
    private final List<String> activeEventNames;

    public ParkState(DatabaseService db, PowerPlant powerPlant, List<Dinosaur> dinosaurs, List<Tourist> activeTourists, List<Vehicle> vehicles) {
        this.db = db;
        this.powerPlant = powerPlant;
        this.dinosaurs = dinosaurs;
        this.activeTourists = activeTourists;
        this.vehicles = vehicles;
        this.activeEventNames = new ArrayList<>();
        this.currentStep = 0;
    }

    public void incrementStep() { this.currentStep++; }
    
    public void clearActiveEvents() {
        this.activeEventNames.clear();
        this.dealsHourActive = false;
        this.currentDiscount = 0.0;
    }

    public void addActiveEvent(String eventName) { this.activeEventNames.add(eventName); }
    
    public long countVehiclesInUse() {
        return vehicles.stream().filter(v -> v.getStatus() != VehicleStatus.AVAILABLE).count();
    }

    // Getters
    public DatabaseService getDb() { return db; }
    public PowerPlant getPowerPlant() { return powerPlant; }
    public List<Dinosaur> getDinosaurs() { return dinosaurs; }
    public List<Tourist> getActiveTourists() { return activeTourists; }
    public List<Vehicle> getVehicles() { return vehicles; }
    public long getCurrentStep() { return currentStep; }
    public boolean isDealsHourActive() { return dealsHourActive; }
    public double getCurrentDiscount() { return currentDiscount; }
    public List<String> getActiveEventNames() { return activeEventNames; }

    // Setters
    public void setDealsHourActive(boolean dealsHourActive) { this.dealsHourActive = dealsHourActive; }
    public void setCurrentDiscount(double currentDiscount) { this.currentDiscount = currentDiscount; }
}