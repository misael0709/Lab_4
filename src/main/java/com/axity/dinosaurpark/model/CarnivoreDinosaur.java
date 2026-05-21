package com.axity.dinosaurpark.model;

public class CarnivoreDinosaur extends Dinosaur {

    public CarnivoreDinosaur(int id, String name, String species) {
        // Costo de alimentación para carnívoros: 500.0
        super(id, name, species, 500.0);
    }

    @Override
    public String getDiet() {
        return "CARNIVORE";
    }

    @Override
    public double getDangerLevel() {
        return 0.9;
    }
    
}
