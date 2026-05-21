package com.axity.dinosaurpark.event;

import com.axity.dinosaurpark.model.Vehicle;
import com.axity.dinosaurpark.model.VehicleStatus;
import com.axity.dinosaurpark.persistence.DatabaseService;
import com.axity.dinosaurpark.simulation.ParkState;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class VehicleFailureEventTest {
    private DatabaseService db;
    private ParkState state;
    private Vehicle vehicle;

    @BeforeEach
    void setUp() {
        db = new DatabaseService("./data/test-vehicle-" + System.currentTimeMillis());
        vehicle = new Vehicle(3);
        List<Vehicle> vehicles = new ArrayList<>();
        vehicles.add(vehicle);
        state = new ParkState(db, null, new ArrayList<>(), new ArrayList<>(), vehicles);
    }

    @AfterEach
    void tearDown() {
        db.close();
    }

    @Test
    void testVehicleBreaks() {
        VehicleFailureEvent event = new VehicleFailureEvent(1.0); // 100% probabilidad
        assertEquals(VehicleStatus.AVAILABLE, vehicle.getStatus());

        event.execute(state, new Random());

        assertEquals(VehicleStatus.BROKEN, vehicle.getStatus());
        assertEquals(1, state.countVehiclesInUse());
    }
}