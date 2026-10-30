package za.ac.cput.vehiclesystem.factory;

import za.ac.cput.vehiclesystem.domain.Agent;
import za.ac.cput.vehiclesystem.domain.Truck;
import za.ac.cput.vehiclesystem.util.Helper;

public class TruckFactory {
    
    public static Truck createTruck(String vehicleId, String model, double price, Agent agent, double loadCapacity) {
        if(!Helper.isNullOrEmpty(vehicleId) && !Helper.isNullOrEmpty(model) && price > 0 && agent != null && loadCapacity > 0) {
            return new Truck.Builder()
                    .setVehicleId(vehicleId)
                    .setModel(model)
                    .setPrice(price)
                    .setAgent(agent)
                    .setLoadCapacity(loadCapacity)
                    .build();
        }
        return null;
    }
}

