package com.axity.dinosaurpark.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class VehicleTest {

    @Test
    void testVehicleStateTransitions() {
        Vehicle vehicle = new Vehicle(3); // 3 steps de reparación
        
        assertEquals(VehicleStatus.AVAILABLE, vehicle.getStatus());
        
        vehicle.use();
        assertEquals(VehicleStatus.IN_USE, vehicle.getStatus());
        
        vehicle.free();
        assertEquals(VehicleStatus.AVAILABLE, vehicle.getStatus());
        
        vehicle.markBroken();
        assertEquals(VehicleStatus.BROKEN, vehicle.getStatus());
        
        // Simular el paso del tiempo
        vehicle.tick(); // Faltan 2
        assertEquals(VehicleStatus.BROKEN, vehicle.getStatus());
        vehicle.tick(); // Falta 1
        vehicle.tick(); // Faltan 0 -> Se auto-repara
        assertEquals(VehicleStatus.AVAILABLE, vehicle.getStatus());
    }
}