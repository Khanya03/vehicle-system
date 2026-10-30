package za.ac.cput.vehiclesystem.factory;

import org.junit.jupiter.api.Test;
import za.ac.cput.vehiclesystem.domain.Agent;
import za.ac.cput.vehiclesystem.domain.Car;
import za.ac.cput.vehiclesystem.domain.Name;

import static org.junit.jupiter.api.Assertions.*;

public class VehicleFactoryTest {
    
    private Agent createTestAgent() {
        Name name = NameFactory.createName("Lisa", "Khanya", "Mpahla");
        return AgentFactory.createAgent("AG001", name, "lisa@gmail.com", "0633292143");
    }
    
    @Test
    void createVehicle_FullModel(){
        Car car = VehicleFactory.createVehicle("BMW", "i118", 50000, createTestAgent(), 4);
        assertNotNull(car);
        
        assertEquals("BMW", car.getVehicleId());
        assertEquals("i118", car.getModel());
        assertEquals(50000, car.getPrice());
        assertEquals(4, car.getNumberOfDoors());
    }
    
    @Test
    void createVehicle_NullModel(){
        Car car = VehicleFactory.createVehicle("BMW", null, 50000, createTestAgent(), 4);
        assertNull(car);
    }
    
    @Test
    void createVehicle_NegativePrice(){
        Car car = VehicleFactory.createVehicle("BMW", "i118", -50000, createTestAgent(), 4);
        assertNull(car);
    }
    
    @Test
    void createVehicle_NullAgent(){
        Car car = VehicleFactory.createVehicle("BMW", "i118", 50000, null, 4);
        assertNull(car);
    }
    
    @Test
    void createVehicle_InvalidNumberOfDoors(){
        Car car = VehicleFactory.createVehicle("BMW", "i118", 50000, createTestAgent(), 0);
        assertNull(car);
    }
}
