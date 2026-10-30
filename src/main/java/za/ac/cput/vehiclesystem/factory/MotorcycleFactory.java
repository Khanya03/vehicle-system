package za.ac.cput.vehiclesystem.factory;

import za.ac.cput.vehiclesystem.domain.Agent;
import za.ac.cput.vehiclesystem.domain.Motorcycle;
import za.ac.cput.vehiclesystem.util.Helper;

public class MotorcycleFactory {
    
    public static Motorcycle createMotorcycle(String vehicleId, String model, double price, Agent agent, int engineCapacity) {
        if(!Helper.isNullOrEmpty(vehicleId) && !Helper.isNullOrEmpty(model) && price > 0 && agent != null && engineCapacity > 0) {
            return new Motorcycle.Builder()
                    .setVehicleId(vehicleId)
                    .setModel(model)
                    .setPrice(price)
                    .setAgent(agent)
                    .setEngineCapacity(engineCapacity)
                    .build();
        }
        return null;
    }
}

