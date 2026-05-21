package com.axity.dinosaurpark.simulation;

import com.axity.dinosaurpark.config.ParkConfig;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SimulationEngineTest {

    @Test
    void testEngineExecutesWithoutErrors() {
        ParkConfig.resetForTesting();
        ParkConfig config = ParkConfig.getInstance();
        SimulationEngine engine = new SimulationEngine(config);

        // Al ejecutar todo el motor de principio a fin, validamos la integración de todas las piezas.
        assertDoesNotThrow(engine::run, "El motor de simulación debe completar todos los steps sin lanzar excepciones");
    }
}