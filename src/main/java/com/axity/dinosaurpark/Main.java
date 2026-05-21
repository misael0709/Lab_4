package com.axity.dinosaurpark;

import com.axity.dinosaurpark.config.ParkConfig;
import com.axity.dinosaurpark.simulation.SimulationEngine;
import org.h2.tools.Shell;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("Iniciando Parque Turístico de Dinosaurios...");
        ParkConfig config = ParkConfig.getInstance();
        SimulationEngine engine = new SimulationEngine(config);
        // 1. Corre simulación
        engine.run();
        
        // 2. Abre la consola SQL de H2 al terminar
        System.out.println("\n=======================================================");
        System.out.println(" SIMULACIÓN TERMINADA. SE ABRIRÁ LA CONSOLA SQL DE H2 PARA CONSULTAS.");
        System.out.println(" Puedes escribir tus consultas apartir de aquí. Escribe 'exit' cuando necesites salir.");
        System.out.println("=======================================================\n");
        
        Shell.main(new String[]{
            "-url", "jdbc:h2:./data/parkdb", 
            "-user", "sa", 
            "-password", ""
        });
    }
}