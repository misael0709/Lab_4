package com.axity.dinosaurpark.zone;

import com.axity.dinosaurpark.config.ParkConfig;
import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.persistence.DatabaseService;
import java.util.Random;

public class PowerPlant implements ParkZone {
    private double currentEnergy;
    private final double consumptionPerStep;
    private final double failureProbability;
    private boolean isOperational;

    public PowerPlant(ParkConfig config) {
        this.currentEnergy = config.getDouble("powerplant.initialEnergy", 100.0);
        this.consumptionPerStep = config.getDouble("powerplant.consumptionPerStep", 1.5);
        this.failureProbability = config.getDouble("powerplant.failureProbability", 0.05);
        this.isOperational = true;
    }

    public void tick(Random rng, DatabaseService db) {
        if (isOperational) {
            currentEnergy = Math.max(0, currentEnergy - consumptionPerStep);
            
            // Verificamos si la planta sufre una falla aleatoria
            if (rng.nextDouble() < failureProbability) {
                triggerFailure();
            }
        }
    }

    public void triggerFailure() {
        this.isOperational = false;
        this.currentEnergy = 0.0;
    }

    public void repair() {
        this.isOperational = true;
        this.currentEnergy = 100.0;
    }

    public boolean isOperational() { return isOperational; }
    public double getCurrentEnergy() { return currentEnergy; }

    // Implementación de la interfaz
    @Override public String getName() { return "Planta de Energía"; }
    @Override public boolean hasCapacity() { return false; }
    @Override public int getCurrentOccupancy() { return 0; }
    @Override public int getMaxCapacity() { return 0; }
    @Override public void enter(Tourist tourist) {}
    @Override public void exit(Tourist tourist) {}
}