package com.axity.dinosaurpark.zone;

import com.axity.dinosaurpark.config.ParkConfig;
import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.persistence.DatabaseService;
import com.axity.dinosaurpark.persistence.RevenueRecord;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class BathroomZone implements ParkZone {
    private final int maxCapacity;
    private final int useDurationSteps;
    private final double spaPrice;
    private final double spaProbability;
    
    // Rastrea cuántos steps le quedan a cada turista en el baño
    private final Map<Tourist, Integer> occupantsTime;

    public BathroomZone(ParkConfig config) {
        this.maxCapacity = config.getInt("bathroom.maxCapacity", 10);
        this.useDurationSteps = config.getInt("bathroom.useDurationSteps", 3);
        this.spaPrice = config.getDouble("bathroom.spaPrice", 20.0);
        this.spaProbability = config.getDouble("bathroom.spaPurchaseProbability", 0.2);
        this.occupantsTime = new HashMap<>();
    }

    public void tryEnter(Tourist tourist, Random rng, DatabaseService db) {
        // Solo entra si hay capacidad y no hay nadie adentro
        if (hasCapacity() && !occupantsTime.containsKey(tourist)) {
            occupantsTime.put(tourist, useDurationSteps);
            tourist.recordVisit(getName());
            
            if (rng.nextDouble() < spaProbability) {
                tourist.spend(spaPrice);
                db.appendRevenue(new RevenueRecord(
                    0, "SPA", spaPrice, tourist.getId(), getName(), LocalDateTime.now()
                ));
            }
        }
    }

    public void tick() {
        // Reducir el tiempo de cada ocupante en 1 step. 
        // Si llega a 0, sale del baño (se elimina del mapa).
        occupantsTime.entrySet().removeIf(entry -> {
            int timeLeft = entry.getValue() - 1;
            entry.setValue(timeLeft);
            return timeLeft <= 0;
        });
    }

    @Override public String getName() { return "Baños y SPA"; }
    @Override public boolean hasCapacity() { return occupantsTime.size() < maxCapacity; }
    @Override public int getCurrentOccupancy() { return occupantsTime.size(); }
    @Override public int getMaxCapacity() { return maxCapacity; }
    @Override public void enter(Tourist tourist) {} // Lógica manejada en tryEnter
    @Override public void exit(Tourist tourist) { occupantsTime.remove(tourist); }
}