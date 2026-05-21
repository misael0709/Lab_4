package com.axity.dinosaurpark.event;

import com.axity.dinosaurpark.model.Vehicle;
import com.axity.dinosaurpark.model.VehicleStatus;
import com.axity.dinosaurpark.persistence.EventRecord;
import com.axity.dinosaurpark.simulation.ParkState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

public class VehicleFailureEvent implements SimulationEvent {
    private final double probability;

    public VehicleFailureEvent(double probability) {
        this.probability = probability;
    }

    @Override public String getName() { return "FALLA_VEHICULO"; }
    @Override public String getDescription() { return "Un vehículo disponible se ha averiado."; }
    @Override public double getProbability() { return probability; }

    @Override
    public void execute(ParkState state, Random rng) {
        List<Vehicle> availableVehicles = state.getVehicles().stream()
                .filter(v -> v.getStatus() == VehicleStatus.AVAILABLE)
                .toList();

        if (!availableVehicles.isEmpty()) {
            Vehicle toBreak = availableVehicles.get(rng.nextInt(availableVehicles.size()));
            toBreak.markBroken();
            state.getDb().appendEvent(toRecord(state.getCurrentStep()));
        }
    }

    @Override
    public EventRecord toRecord(long step) {
        return new EventRecord(step, getName(), getDescription(), "Vehículos de mantenimiento", LocalDateTime.now());
    }
}