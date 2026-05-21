package com.axity.dinosaurpark.persistence;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class DatabaseServiceTest {

    @Test
    void testAppendRecordsDoesNotThrow() {
        // Base de datos temporal única para este test
        String path = "./data/test-db-" + System.currentTimeMillis();
        DatabaseService db = new DatabaseService(path);

        // Verificamos que la conexión no lance excepciones
        assertDoesNotThrow(() -> {
            db.appendRevenue(new RevenueRecord(0, "TEST", 10.0, 1, "Hub", LocalDateTime.now()));
            db.appendExpense(new ExpenseRecord(0, "TEST", 20.0, "Desc", LocalDateTime.now()));
            db.appendEvent(new EventRecord(0, "TEST_EV", "Desc", "None", LocalDateTime.now()));
        }, "Las operaciones de inserción en H2 no deben fallar");

        db.close();
    }
}