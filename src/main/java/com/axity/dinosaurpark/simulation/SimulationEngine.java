package com.axity.dinosaurpark.simulation;

import com.axity.dinosaurpark.config.ParkConfig;
import com.axity.dinosaurpark.event.*;
import com.axity.dinosaurpark.model.*;
import com.axity.dinosaurpark.monitoring.ParkMonitor;
import com.axity.dinosaurpark.persistence.DatabaseService;
import com.axity.dinosaurpark.zone.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimulationEngine {
    private final ParkConfig config;
    private final ParkState state;
    private final Random rng;
    
    // Zonas
    private final ArrivalZone arrivalZone;
    private final CentralHub centralHub;
    private final BathroomZone bathroomZone;
    private final ObservationEnclosure basicEnclosure;
    private final ObservationEnclosure premiumEnclosure;
    private final ObservationEnclosure vipEnclosure;
    
    // Trabajadores
    private final List<Guard> guards;
    private final List<Technician> technicians;
    
    // Eventos
    private final List<SimulationEvent> allEvents;
    
    public SimulationEngine(ParkConfig config) {
        this.config = config;
        this.rng = new Random(); // Instancia sin semilla para no-determinismo
        
        DatabaseService db = new DatabaseService(config.getString("db.path", "./data/parkdb"));
        PowerPlant powerPlant = new PowerPlant(config);
        
        List<Dinosaur> dinosaurs = initDinosaurs(config);
        List<Tourist> activeTourists = new ArrayList<>();
        List<Vehicle> vehicles = initVehicles(config);
        
        this.state = new ParkState(db, powerPlant, dinosaurs, activeTourists, vehicles);
        
        this.arrivalZone = new ArrivalZone(config);
        this.centralHub = new CentralHub(config);
        this.bathroomZone = new BathroomZone(config);
        this.basicEnclosure = new ObservationEnclosure(ExperienceType.BASIC, config);
        this.premiumEnclosure = new ObservationEnclosure(ExperienceType.PREMIUM, config);
        this.vipEnclosure = new ObservationEnclosure(ExperienceType.VIP, config);
        
        this.guards = new ArrayList<>();
        for (int i = 0; i < config.getInt("workers.guards", 3); i++) {
            guards.add(new Guard(i + 1, "Guardia " + (i + 1), config.getDouble("workers.dailySalary", 150.0)));
        }
        
        this.technicians = new ArrayList<>();
        for (int i = 0; i < config.getInt("workers.technicians", 2); i++) {
            technicians.add(new Technician(i + 1, "Técnico " + (i + 1), config.getDouble("workers.dailySalary", 150.0)));
        }
        
        this.allEvents = List.of(
            new DinosaurEscapeEvent(config.getDouble("event.escape.probability", 0.05)),
            new BlackoutEvent(config.getDouble("event.blackout.probability", 0.03)),
            new StormEvent(config.getDouble("event.storm.probability", 0.04)),
            new DealsHourEvent(config.getDouble("event.deals.probability", 0.08)),
            new VehicleFailureEvent(config.getDouble("event.vehicleFailure.probability", 0.06))
        );
    }
    
    private List<Dinosaur> initDinosaurs(ParkConfig config) {
        List<Dinosaur> list = new ArrayList<>();
        int idCounter = 1;
        int carnivores = config.getInt("dinosaurs.carnivores", 5);
        for (int i = 0; i < carnivores; i++) list.add(new CarnivoreDinosaur(idCounter++, "T-Rex " + idCounter, "Tyrannosaurus"));
        
        int herbivores = config.getInt("dinosaurs.herbivores", 15);
        for (int i = 0; i < herbivores; i++) list.add(new HerbivoreDinosaur(idCounter++, "Triceratops " + idCounter, "Triceratops"));
        return list;
    }

    private List<Vehicle> initVehicles(ParkConfig config) {
        List<Vehicle> list = new ArrayList<>();
        int count = config.getInt("vehicles.count", 4);
        int repairSteps = config.getInt("vehicles.repairSteps", 5);
        for (int i = 0; i < count; i++) list.add(new Vehicle(repairSteps));
        return list;
    }

    public void run() {
        int totalSteps = config.getTotalSteps();
        int batchSize = config.getInt("simulation.arrivalBatchSize", 5);
        int totalTourists = config.getInt("tourists", 50);
        int monitoringInterval = config.getInt("monitoring.intervalSteps", 10);
        
        for (int i = 0; i < totalTourists; i++) {
            arrivalZone.addTouristToLine(new Tourist(i + 1, "Turista " + (i + 1)));
        }
        
        for (int step = 1; step <= totalSteps; step++) {
            state.incrementStep();
            state.clearActiveEvents();
            
            // A. LLEGADAS
            List<Tourist> arrived = arrivalZone.processBatch(batchSize, state.getDb(), state.getCurrentDiscount());
            state.getActiveTourists().addAll(arrived);
            
            // B. MOVIMIENTO DE TURISTAS
            for (Tourist t : new ArrayList<>(state.getActiveTourists())) {
                centralHub.visit(t, rng, state.getDb(), state.getCurrentDiscount());
                bathroomZone.tryEnter(t, rng, state.getDb());
                
                double r = rng.nextDouble();
                if (r < 0.5) basicEnclosure.visit(t, rng, state.getDb());
                else if (r < 0.8) premiumEnclosure.visit(t, rng, state.getDb());
                else vipEnclosure.visit(t, rng, state.getDb());
            }
            
            // C. TICKS ZONAS Y VEHICULOS
            bathroomZone.tick();
            state.getPowerPlant().tick(rng, state.getDb());
            state.getVehicles().forEach(Vehicle::tick);
            
            // D. EVENTOS PROBABILÍSTICOS
            for (SimulationEvent event : allEvents) {
                if (rng.nextDouble() < event.getProbability()) {
                    event.execute(state, rng);
                    // ¡Esta es la línea que faltaba! Registra el evento en el estado actual
                    state.addActiveEvent(event.getName()); 
                }
            }
            
            // E. TRABAJADORES
            guards.forEach(g -> g.recaptureEscapedDinosaurs(state.getDinosaurs()));
            technicians.forEach(t -> t.repairIfNeeded(state.getPowerPlant(), state.getVehicles()));
            
            // F. MONITOREO
            if (state.getCurrentStep() % monitoringInterval == 0) {
                ParkMonitor.displaySnapshot(state);
            }
        }
        
        state.getDb().close();
        System.out.println("Simulación terminada con éxito.");
    }
}