package za.ac.cput.vehiclesystem.domain;

import jakarta.persistence.Entity;

@Entity
public class Car extends Vehicle {

    private int numberOfDoors;

    protected Car() {}

    protected Car(Builder builder) {
        super(builder);
        this.numberOfDoors = builder.numberOfDoors;
    }

    public int getNumberOfDoors() {
        return numberOfDoors;
    }

    @Override
    public String toString() {
        return "Car{" +
                "numberOfDoors=" + numberOfDoors +
                ", vehicleId='" + getVehicleId() + '\'' +
                ", model='" + getModel() + '\'' +
                ", price=" + getPrice() +
                ", agent=" + getAgent() +
                '}';
    }

    public static class Builder{
        protected int numberOfDoors;
        protected String vehicleId;
        protected String model;
        protected double price;
        protected Agent agent;

        public Builder setNumberOfDoors(int numberOfDoors) {
            this.numberOfDoors = numberOfDoors;
            return this;
        }

        public Builder setAgent(Agent agent) {
            this.agent = agent;
            return this;
        }

        public Builder setPrice(double price) {
            this.price = price;
            return this;
        }

        public Builder setModel(String model) {
            this.model = model;
            return this;
        }

        public Builder setVehicleId(String vehicleId) {
            this.vehicleId = vehicleId;
            return this;
        }
        public Car build() {
            return new Car(this);
        }
    }
}