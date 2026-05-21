package com.axity.dinosaurpark.model;

public class Vehicle {
    private VehicleStatus status;
    private int repairCountdown;
    private final int repairSteps;

    public Vehicle(int repairSteps) {
        this.status = VehicleStatus.AVAILABLE;
        this.repairCountdown = 0;
        this.repairSteps = repairSteps;
    }

    public void use() {
        if (this.status == VehicleStatus.AVAILABLE) {
            this.status = VehicleStatus.IN_USE;
        }
    }

    public void free() {
        if (this.status == VehicleStatus.IN_USE) {
            this.status = VehicleStatus.AVAILABLE;
        }
    }

    public void markBroken() {
        this.status = VehicleStatus.BROKEN;
        this.repairCountdown = this.repairSteps;
    }

    public void tick() {
        if (this.status == VehicleStatus.BROKEN) {
            repairCountdown--;
            if (repairCountdown <= 0) {
                this.status = VehicleStatus.AVAILABLE;
            }
        }
    }

    public VehicleStatus getStatus() { return status; }
}