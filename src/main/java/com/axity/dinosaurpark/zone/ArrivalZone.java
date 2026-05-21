package com.axity.dinosaurpark.zone;

import com.axity.dinosaurpark.config.ParkConfig;
import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.model.TouristStatus;
import com.axity.dinosaurpark.persistence.DatabaseService;
import com.axity.dinosaurpark.persistence.RevenueRecord;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ArrivalZone implements ParkZone {
    private final int maxCapacity;
    private final double ticketPrice;
    private final Queue<Tourist> waitingLine;
    private final List<Tourist> currentOccupants;

    public ArrivalZone(ParkConfig config) {
        this.maxCapacity = config.getInt("arrival.maxCapacity", 30);
        this.ticketPrice = config.getDouble("arrival.ticketPrice", 25.0);
        this.waitingLine = new LinkedList<>();
        this.currentOccupants = new ArrayList<>();
    }

    public void addTouristToLine(Tourist t) {
        waitingLine.offer(t);
    }

    public List<Tourist> processBatch(int batchSize, DatabaseService db, double discount) {
        List<Tourist> processed = new ArrayList<>();
        int count = 0;
        
        while (!waitingLine.isEmpty() && count < batchSize && hasCapacity()) {
            Tourist t = waitingLine.poll();
            
            double finalPrice = ticketPrice * (1.0 - discount);
            t.spend(finalPrice);
            t.setStatus(TouristStatus.IN_PARK);
            t.recordVisit(getName());
            
            enter(t);
            processed.add(t);
            
            db.appendRevenue(new RevenueRecord(
                0, "TICKET", finalPrice, t.getId(), getName(), LocalDateTime.now()
            ));
            
            count++;
        }
        return processed;
    }

    @Override public String getName() { return "Zona de Arribo"; }
    @Override public boolean hasCapacity() { return currentOccupants.size() < maxCapacity; }
    @Override public int getCurrentOccupancy() { return currentOccupants.size(); }
    @Override public int getMaxCapacity() { return maxCapacity; }
    
    @Override
    public void enter(Tourist tourist) {
        if (hasCapacity()) currentOccupants.add(tourist);
    }

    @Override
    public void exit(Tourist tourist) {
        currentOccupants.remove(tourist);
    }
}