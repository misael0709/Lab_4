package com.axity.dinosaurpark.event;

import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.persistence.EventRecord;
import com.axity.dinosaurpark.persistence.ExpenseRecord;
import com.axity.dinosaurpark.simulation.ParkState;

import java.time.LocalDateTime;
import java.util.Random;

public class StormEvent implements SimulationEvent {
    private final double probability;

    public StormEvent(double probability) {
        this.probability = probability;
    }

    @Override public String getName() { return "TORMENTA_TORRENCIAL"; }
    @Override public String getDescription() { return "El parque se detiene por clima extremo."; }
    @Override public double getProbability() { return probability; }

    @Override
    public void execute(ParkState state, Random rng) {
        for (Tourist t : state.getActiveTourists()) {
            t.recordVisit("Evacuación");
        }
        state.getDb().appendExpense(new ExpenseRecord(
            0, "OPERATIVO", 500.0, "Logística de evacuación por tormenta", LocalDateTime.now()
        ));
        state.getDb().appendEvent(toRecord(state.getCurrentStep()));
    }

    @Override
    public EventRecord toRecord(long step) {
        return new EventRecord(step, getName(), getDescription(), "Todos los turistas", LocalDateTime.now());
    }
}