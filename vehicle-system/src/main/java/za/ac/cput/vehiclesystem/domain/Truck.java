package za.ac.cput.vehiclesystem.domain;

import jakarta.persistence.Entity;

@Entity
public class Truck extends Vehicle {

       private double loadCapacity;

       protected Truck() {}

       protected Truck(Builder builder) {
           super(builder);
           this.loadCapacity = builder.loadCapacity;
       }

    public double getLoadCapacity() {
        return loadCapacity;
    }

    @Override
    public String toString() {
        return "Truck{" +
                "loadCapacity=" + loadCapacity +
                ", vehicleId='" + getVehicleId() + '\'' +
                ", model='" + getModel() + '\'' +
                ", price=" + getPrice() +
                ", agent=" + getAgent() +
                '}';
    }

    public static class Builder{
        protected double loadCapacity;
        protected String vehicleId;
        protected String model;
        protected double price;
        protected Agent agent;

        public Builder setLoadCapacity(double loadCapacity) {
            this.loadCapacity = loadCapacity;
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
        public Truck build() {
            return new Truck(this);
        }
    }
}