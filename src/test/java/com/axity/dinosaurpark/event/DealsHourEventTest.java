package com.axity.dinosaurpark.event;

import com.axity.dinosaurpark.persistence.DatabaseService;
import com.axity.dinosaurpark.simulation.ParkState;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class DealsHourEventTest {
    private DatabaseService db;
    private ParkState state;

    @BeforeEach
    void setUp() {
        db = new DatabaseService("./data/test-deals-" + System.currentTimeMillis());
        state = new ParkState(db, null, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }

    @AfterEach
    void tearDown() {
        db.close();
    }

    @Test
    void testEventActivatesDiscount() {
        DealsHourEvent event = new DealsHourEvent(1.0); // 100% de probabilidad para forzar el test
        event.execute(state, new Random());

        assertTrue(state.isDealsHourActive());
        assertEquals(0.30, state.getCurrentDiscount());

        // Verificamos que el reset al inicio de cada step funcione
        state.clearActiveEvents();
        assertFalse(state.isDealsHourActive());
        assertEquals(0.0, state.getCurrentDiscount());
    }
}