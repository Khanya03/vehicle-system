package za.ac.cput.vehiclesystem.domain;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;


@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
@JsonSubTypes({
        @JsonSubTypes.Type(Car.class),
        @JsonSubTypes.Type(Truck.class),
        @JsonSubTypes.Type(Motorcycle.class)
})
public abstract class Vehicle {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected String vehicleId;
    protected String model;
    protected double price;

    @ManyToOne
    protected Agent agent;
    
    protected Vehicle(){}

    protected Vehicle(Car.Builder builder){
        this.vehicleId = builder.vehicleId;
        this.model = builder.model;
        this.price = builder.price;
        this.agent = builder.agent;
    }

    protected Vehicle(Truck.Builder builder){
        this.vehicleId = builder.vehicleId;
        this.model = builder.model;
        this.price = builder.price;
        this.agent = builder.agent;
    }

    protected Vehicle(Motorcycle.Builder builder){
        this.vehicleId = builder.vehicleId;
        this.model = builder.model;
        this.price = builder.price;
        this.agent = builder.agent;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public String getModel() {
        return model;
    }

    public double getPrice() {
        return price;
    }

    public Agent getAgent() {
        return agent;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "vehicleId='" + vehicleId + '\'' +
                ", model='" + model + '\'' +
                ", price=" + price +
                ", agent=" + agent +
                '}';
    }
}