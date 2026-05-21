package com.axity.dinosaurpark.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TouristTest {

    @Test
    void testTouristLifecycle() {
        Tourist tourist = new Tourist(1, "Alan Grant");
        
        assertEquals(1, tourist.getId());
        assertEquals("Alan Grant", tourist.getName());
        assertEquals(TouristStatus.WAITING, tourist.getStatus());
        assertEquals(0.0, tourist.getMoneySpent());
        
        tourist.spend(45.5);
        assertEquals(45.5, tourist.getMoneySpent());
        
        tourist.recordVisit("Recinto Central");
        assertTrue(tourist.getVisitedZones().contains("Recinto Central"));
        
        tourist.setStatus(TouristStatus.IN_PARK);
        assertEquals(TouristStatus.IN_PARK, tourist.getStatus());
    }
}