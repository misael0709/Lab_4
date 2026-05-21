package com.axity.dinosaurpark.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DinosaurTest {

    @Test
    void testCarnivoreLifecycle() {
        Dinosaur rex = new CarnivoreDinosaur(1, "Rexy", "Tyrannosaurus");
        
        assertEquals("CARNIVORE", rex.getDiet());
        assertEquals(0.9, rex.getDangerLevel());
        assertEquals(DinosaurStatus.IN_ENCLOSURE, rex.getStatus());
        assertEquals(500.0, rex.getFeedingCostPerDay());
        
        rex.escape();
        assertEquals(DinosaurStatus.ESCAPED, rex.getStatus());
        
        rex.recapture();
        assertEquals(DinosaurStatus.RECAPTURED, rex.getStatus());
        
        rex.returnToEnclosure();
        assertEquals(DinosaurStatus.IN_ENCLOSURE, rex.getStatus());
    }

    @Test
    void testHerbivoreProperties() {
        Dinosaur trike = new HerbivoreDinosaur(2, "Cera", "Triceratops");
        
        assertEquals("HERBIVORE", trike.getDiet());
        assertEquals(0.2, trike.getDangerLevel());
        assertEquals(200.0, trike.getFeedingCostPerDay());
    }
}