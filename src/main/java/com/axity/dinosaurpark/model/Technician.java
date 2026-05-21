package com.axity.dinosaurpark.model;

import java.util.List;
import java.util.Optional;
import com.axity.dinosaurpark.zone.PowerPlant;

public class Technician extends Worker {
    public Technician(int id, String name, double dailySalary) {
        super(id, name, dailySalary);
    }

    @Override
    public String getRole() {
        return "TECHNICIAN";
    }

    // Lógica del nivel intermedio: necesita un vehículo AVAILABLE para reparar
    public void repairIfNeeded(PowerPlant plant, List<Vehicle> vehicles) {
        if (!plant.isOperational()) {
            Optional<Vehicle> available = vehicles.stream()
                .filter(v -> v.getStatus() == VehicleStatus.AVAILABLE)
                .findFirst();
            
            if (available.isPresent()) {
                available.get().use();
                plant.repair();
                available.get().free();
            }
        }
    }
}