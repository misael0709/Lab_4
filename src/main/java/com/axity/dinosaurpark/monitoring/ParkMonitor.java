package com.axity.dinosaurpark.monitoring;

import com.axity.dinosaurpark.model.DinosaurStatus;
import com.axity.dinosaurpark.simulation.ParkState;

public class ParkMonitor {
    public static void displaySnapshot(ParkState state) {
        System.out.println("\n========================================");
        System.out.println("   MONITOR DEL PARQUE - STEP " + state.getCurrentStep());
        System.out.println("========================================");
        
        // Métrica 1: Turistas activos
        System.out.println("1. Turistas activos: " + state.getActiveTourists().size());
        
        // Métrica 2: Dinosaurios en encierro
        long inEnclosure = state.getDinosaurs().stream()
                .filter(d -> d.getStatus() == DinosaurStatus.IN_ENCLOSURE).count();
        System.out.println("2. Dinosaurios seguros: " + inEnclosure + "/" + state.getDinosaurs().size());
        
        // Métrica 3: Energía disponible
        System.out.println("3. Energía de la Planta: " + String.format("%.1f", state.getPowerPlant().getCurrentEnergy()) + "%");
        
        // Métrica 4: Eventos activos
        String events = state.getActiveEventNames().isEmpty() ? "Ninguno" : String.join(", ", state.getActiveEventNames());
        System.out.println("4. Eventos activos: " + events);
        
        // Métrica 5: Vehículos no disponibles (en uso o rotos)
        System.out.println("5. Vehículos ocupados/averiados: " + state.countVehiclesInUse() + "/" + state.getVehicles().size());
        System.out.println("========================================\n");
    }
}