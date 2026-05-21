package com.axity.dinosaurpark.config;

import java.io.InputStream;
import java.util.Properties;

public final class ParkConfig {

    private static ParkConfig instance;
    private final Properties props;

    // Constructor PRIVADO — nadie puede hacer "new ParkConfig()" desde afuera
    private ParkConfig() {
        props = new Properties();
        // Carga park.properties usando el ClassLoader para que lo encuentre en src/main/resources
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("park.properties")) {
            if (input == null) {
                throw new RuntimeException("No se pudo encontrar park.properties en el classpath");
            }
            props.load(input);
        } catch (Exception ex) {
            throw new RuntimeException("Error al cargar la configuración del parque", ex);
        }
    }

    // Punto de acceso global — crea la instancia solo la primera vez que se llama
    public static ParkConfig getInstance() {
        if (instance == null) {
            instance = new ParkConfig();
        }
        return instance;
    }

    // Métodos de lectura
    public int getInt(String key, int defaultValue) {
        String value = props.getProperty(key);
        return value != null ? Integer.parseInt(value) : defaultValue;
    }

    public double getDouble(String key, double defaultValue) {
        String value = props.getProperty(key);
        return value != null ? Double.parseDouble(value) : defaultValue;
    }

    public String getString(String key, String defaultValue) {
        return props.getProperty(key, defaultValue);
    }

    public int getTotalSteps() {
        return getInt("simulation.totalSteps", 100);
    }

    // Solo para tests — permite limpiar la instancia en memoria entre ejecuciones de JUnit
    public static void resetForTesting() {
        instance = null;
    }
}