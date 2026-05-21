package com.axity.dinosaurpark.monitoring;

import com.axity.dinosaurpark.config.ParkConfig;
import com.axity.dinosaurpark.model.DinosaurStatus;
import com.axity.dinosaurpark.simulation.ParkState;

public class ParkMonitor {
    
    public static void printStartBanner(ParkConfig config) {
        System.out.println("\n=== INICIANDO SIMULACIÓN: Parque Turístico de Dinosaurios (Intermedio) ===");
        System.out.println("Turistas: " + config.getInt("tourists", 50) + 
                           " | Dinosaurios: " + (config.getInt("dinosaurs.carnivores", 5) + config.getInt("dinosaurs.herbivores", 15)) +
                           " | Vehículos: " + config.getInt("vehicles.count", 4) +
                           " | Steps: " + config.getTotalSteps());
        System.out.println("========================================================================\n");
    }

    public static void displaySnapshot(ParkState state) {
        System.out.println("----------------------------------------");
        System.out.println("MONITOR - Step " + state.getCurrentStep());
        System.out.println("----------------------------------------");
        
        System.out.println("1. Turistas activos:       " + state.getActiveTourists().size());
        
        long inEnclosure = state.getDinosaurs().stream()
                .filter(d -> d.getStatus() == DinosaurStatus.IN_ENCLOSURE).count();
        System.out.println("2. Dinosaurios en encierro:" + inEnclosure + "/" + state.getDinosaurs().size());
        
        System.out.println("3. Energía disponible:     " + String.format("%.1f", state.getPowerPlant().getCurrentEnergy()) + "%");
        
        String events = state.getActiveEventNames().isEmpty() ? "Ninguno" : String.join(", ", state.getActiveEventNames());
        System.out.println("4. Eventos activos:        " + events);
        
        System.out.println("5. Vehículos no disponibles:" + state.countVehiclesInUse() + "/" + state.getVehicles().size());
        System.out.println("----------------------------------------\n");
    }
    
    public static void printEndSummary(ParkState state) {
        System.out.println("\n=== SIMULACIÓN TERMINADA CON ÉXITO ===");
        System.out.println("Pasos simulados: " + state.getCurrentStep());
        System.out.println("Turistas que ingresaron: " + state.getActiveTourists().size());
        System.out.println("Energía final de la planta: " + String.format("%.1f", state.getPowerPlant().getCurrentEnergy()) + "%");
        System.out.println("======================================\n");
    }
}