package com.axity.dinosaurpark.zone;

import com.axity.dinosaurpark.config.ParkConfig;
import com.axity.dinosaurpark.model.SatisfactionSurvey;
import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.persistence.DatabaseService;
import com.axity.dinosaurpark.persistence.RevenueRecord;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ObservationEnclosure implements ParkZone {
    private final ExperienceType type;
    private final int maxVisitors;
    private final double entryFee;
    private final List<Tourist> currentOccupants;

    public ObservationEnclosure(ExperienceType type, ParkConfig config) {
        this.type = type;
        // Leemos las properties dinámicamente
        String prefix = "enclosure." + type.name().toLowerCase();
        this.maxVisitors = config.getInt(prefix + ".maxVisitors", 10);
        this.entryFee = config.getDouble(prefix + ".entryFee", 10.0);
        this.currentOccupants = new ArrayList<>();
    }

    public void visit(Tourist tourist, Random rng, DatabaseService db) {
        if (hasCapacity() && !currentOccupants.contains(tourist)) {
            enter(tourist);
            tourist.spend(entryFee);
            tourist.recordVisit(getName());
            
            db.appendRevenue(new RevenueRecord(
                0, "ENCLOSURE_" + type.name(), entryFee, tourist.getId(), getName(), LocalDateTime.now()
            ));
            
            conductSurvey(tourist, rng);
            
            exit(tourist);
        }
    }

    public SatisfactionSurvey conductSurvey(Tourist tourist, Random rng) {
        int min = 1, max = 5;
        switch (type) {
            case BASIC -> { min = 1; max = 3; }
            case PREMIUM -> { min = 2; max = 4; }
            case VIP -> { min = 3; max = 5; }
        }
        int score = rng.nextInt((max - min) + 1) + min;
        return new SatisfactionSurvey(tourist.getId(), getName(), score);
    }

    @Override public String getName() { return "Encierro " + type.name(); }
    @Override public boolean hasCapacity() { return currentOccupants.size() < maxVisitors; }
    @Override public int getCurrentOccupancy() { return currentOccupants.size(); }
    @Override public int getMaxCapacity() { return maxVisitors; }
    @Override public void enter(Tourist tourist) { currentOccupants.add(tourist); }
    @Override public void exit(Tourist tourist) { currentOccupants.remove(tourist); }
}