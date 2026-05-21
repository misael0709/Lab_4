package com.axity.dinosaurpark.model;

public class HerbivoreDinosaur extends Dinosaur {
    
    public HerbivoreDinosaur(int id, String name, String species) {
        // Costo de alimentación para herbívoros: 200.0
        super(id, name, species, 200.0);
    }

    @Override
    public String getDiet() {
        return "HERBIVORE";
    }

    @Override
    public double getDangerLevel() {
        return 0.2;
    }
}