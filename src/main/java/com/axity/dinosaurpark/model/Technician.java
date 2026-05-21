package com.axity.dinosaurpark.model;

import java.util.List;
import com.axity.dinosaurpark.zone.PowerPlant;

public class Technician extends Worker {
    public Technician(int id, String name, double dailySalary) {
        super(id, name, dailySalary);
    }

    @Override
    public String getRole() {
        return "TECHNICIAN";
    }

    public void repairIfNeeded(PowerPlant powerPlant, List<Vehicle> vehicles) {
        if (powerPlant.isBroken()) {
            Vehicle vehicleToUse = vehicles.stream()
                    .filter(v -> v.getStatus() == VehicleStatus.AVAILABLE)
                    .findFirst()
                    .orElse(null);

            if (vehicleToUse != null) {
                vehicleToUse.use();
                powerPlant.repair();
                System.out.println("[" + getName() + "] Reparó la planta usando Vehículo"); 
                vehicleToUse.free();
            }
        }
    }
}