package com.axity.dinosaurpark.zone;

import com.axity.dinosaurpark.config.ParkConfig;
import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.persistence.DatabaseService;
import com.axity.dinosaurpark.persistence.RevenueRecord;

import java.time.LocalDateTime;
import java.util.Random;

public class CentralHub implements ParkZone {
    private final double souvenirPrice;
    private final double purchaseProbability;

    public CentralHub(ParkConfig config) {
        this.souvenirPrice = config.getDouble("hub.souvenirPrice", 15.0);
        this.purchaseProbability = config.getDouble("hub.souvenirPurchaseProbability", 0.4);
    }

    public void visit(Tourist tourist, Random rng, DatabaseService db, double discount) {
        tourist.recordVisit(getName());
        
        // Tira el dado para ver si compra el souvenir
        if (rng.nextDouble() < purchaseProbability) {
            double finalPrice = souvenirPrice * (1.0 - discount);
            tourist.spend(finalPrice);
            
            db.appendRevenue(new RevenueRecord(
                0, "SOUVENIR", finalPrice, tourist.getId(), getName(), LocalDateTime.now()
            ));
        }
    }

    // El hub central no tiene límite de capacidad
    @Override public String getName() { return "Recinto Central"; }
    @Override public boolean hasCapacity() { return true; }
    @Override public int getCurrentOccupancy() { return 0; }
    @Override public int getMaxCapacity() { return Integer.MAX_VALUE; }
    @Override public void enter(Tourist tourist) {}
    @Override public void exit(Tourist tourist) {}
}