package za.ac.cput.vehiclesystem.service;

import za.ac.cput.vehiclesystem.domain.Vehicle;
import java.util.List;

public interface IVehicleService {

	Vehicle save(Vehicle vehicle);

	Vehicle read(String vehicleId);

	Vehicle update(Vehicle vehicle);

	boolean delete(String vehicleId);

	List<Vehicle> findAll();

	List<Vehicle> findByModel(String model);

	List<Vehicle> findByAgentId(String agentId);

	long countByAgentId(String agentId);

}