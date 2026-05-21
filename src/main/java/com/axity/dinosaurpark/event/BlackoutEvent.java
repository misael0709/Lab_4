package com.axity.dinosaurpark.event;

import com.axity.dinosaurpark.persistence.EventRecord;
import com.axity.dinosaurpark.persistence.ExpenseRecord;
import com.axity.dinosaurpark.simulation.ParkState;

import java.time.LocalDateTime;
import java.util.Random;

public class BlackoutEvent implements SimulationEvent {
    private final double probability;

    public BlackoutEvent(double probability) {
        this.probability = probability;
    }

    @Override public String getName() { return "APAGON_MASIVO"; }
    @Override public String getDescription() { return "Falla crítica en la planta de energía."; }
    @Override public double getProbability() { return probability; }

    @Override
    public void execute(ParkState state, Random rng) {
        state.getPowerPlant().triggerFailure();
        state.getDb().appendExpense(new ExpenseRecord(
            0, "EMERGENCIA", 2000.0, "Reparación por apagón masivo", LocalDateTime.now()
        ));
        state.getDb().appendEvent(toRecord(state.getCurrentStep()));
    }

    @Override
    public EventRecord toRecord(long step) {
        return new EventRecord(step, getName(), getDescription(), "Planta de Energía", LocalDateTime.now());
    }
}