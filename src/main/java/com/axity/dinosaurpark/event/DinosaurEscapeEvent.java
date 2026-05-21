package com.axity.dinosaurpark.event;

import com.axity.dinosaurpark.model.Dinosaur;
import com.axity.dinosaurpark.model.DinosaurStatus;
import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.model.TouristStatus;
import com.axity.dinosaurpark.persistence.EventRecord;
import com.axity.dinosaurpark.simulation.ParkState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

public class DinosaurEscapeEvent implements SimulationEvent {
    private final double probability;

    public DinosaurEscapeEvent(double probability) {
        this.probability = probability;
    }

    @Override public String getName() { return "ESCAPE_DINOSAURIO"; }
    @Override public String getDescription() { return "Un dinosaurio ha escapado del encierro."; }
    @Override public double getProbability() { return probability; }

    @Override
    public void execute(ParkState state, Random rng) {
        List<Dinosaur> inEnclosure = state.getDinosaurs().stream()
                .filter(d -> d.getStatus() == DinosaurStatus.IN_ENCLOSURE)
                .toList();

        if (!inEnclosure.isEmpty()) {
            Dinosaur escaper = inEnclosure.get(rng.nextInt(inEnclosure.size()));
            escaper.escape();

            if (rng.nextDouble() < escaper.getDangerLevel()) {
                List<Tourist> activeTourists = state.getActiveTourists();
                if (!activeTourists.isEmpty()) {
                    Tourist victim = activeTourists.get(rng.nextInt(activeTourists.size()));
                    victim.setStatus(TouristStatus.ATTACKED);
                }
            }
            state.getDb().appendEvent(toRecord(state.getCurrentStep()));
        }
    }

    @Override
    public EventRecord toRecord(long step) {
        return new EventRecord(step, getName(), getDescription(), "Dinosaurios, Turistas", LocalDateTime.now());
    }
}