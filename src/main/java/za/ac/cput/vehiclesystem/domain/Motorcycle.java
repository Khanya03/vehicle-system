package za.ac.cput.vehiclesystem.domain;

import jakarta.persistence.Entity;

@Entity
public class Motorcycle extends Vehicle {

    private int engineCapacity;

    private Motorcycle() {}

    private Motorcycle(Builder builder) {
        super(builder);
        this.engineCapacity = builder.engineCapacity;
    }

    public int getEngineCapacity() {
        return engineCapacity;
    }

    @Override
    public String toString() {
        return "Motorcycle{" +
                "engineCapacity=" + engineCapacity +
                ", vehicleId='" + getVehicleId() + '\'' +
                ", model='" + getModel() + '\'' +
                ", price=" + getPrice() +
                ", agent=" + getAgent() +
                '}';
    }

    public static class Builder{
        protected int engineCapacity;
        protected String vehicleId;
        protected String model;
        protected double price;
        protected Agent agent;

        public Builder setEngineCapacity(int engineCapacity) {
            this.engineCapacity = engineCapacity;
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
        public Motorcycle build() {
            return new Motorcycle(this);
        }
    }
}
