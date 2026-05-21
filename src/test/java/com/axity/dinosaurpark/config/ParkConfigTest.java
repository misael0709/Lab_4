package com.axity.dinosaurpark.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ParkConfigTest {

    @BeforeEach
    void setUp() {
        ParkConfig.resetForTesting();
    }

    @Test
    void testSingletonInstance() {
        ParkConfig instance1 = ParkConfig.getInstance();
        ParkConfig instance2 = ParkConfig.getInstance();
        assertNotNull(instance1);
        assertSame(instance1, instance2, "El patrón Singleton debe devolver siempre la misma instancia");
    }

    @Test
    void testReadProperties() {
        ParkConfig config = ParkConfig.getInstance();
        // Verifica que lea valores reales y aplique los defaults si no existen
        assertTrue(config.getInt("tourists", 10) > 0);
        assertTrue(config.getDouble("arrival.ticketPrice", 10.0) > 0.0);
        assertEquals("default_value", config.getString("llave.que.no.existe", "default_value"));
    }
}