package za.ac.cput.vehiclesystem.service;

import org.springframework.stereotype.Service;
import za.ac.cput.vehiclesystem.domain.Vehicle;
import za.ac.cput.vehiclesystem.repository.VehicleRepository;

import java.util.List;

@Service
public class VehicleService implements IVehicleService {

	private final VehicleRepository vehicleRepository;

	public VehicleService(VehicleRepository vehicleRepository) {
		this.vehicleRepository = vehicleRepository;
	}

	@Override
	public Vehicle save(Vehicle vehicle) {
		if (vehicle == null) return null;
		return vehicleRepository.save(vehicle);
	}

	@Override
	public Vehicle read(String vehicleId) {
		return vehicleRepository.findById(vehicleId).orElse(null);
	}

	@Override
	public Vehicle update(Vehicle vehicle) {
		if (vehicle == null || vehicle.getVehicleId() == null) return null;
		if (!vehicleRepository.existsById(vehicle.getVehicleId())) return null;
		return vehicleRepository.save(vehicle);
	}

	@Override
	public boolean delete(String vehicleId) {
		if (!vehicleRepository.existsById(vehicleId)) return false;
		vehicleRepository.deleteById(vehicleId);
		return !vehicleRepository.existsById(vehicleId);
	}

	@Override
	public List<Vehicle> findAll() {
		return vehicleRepository.findAll();
	}

	@Override
	public List<Vehicle> findByModel(String model) {
		return vehicleRepository.findByModel(model);
	}

	@Override
	public List<Vehicle> findByAgentId(String agentId) {
		return vehicleRepository.findByAgentAgentId(agentId);
	}

	@Override
	public long countByAgentId(String agentId) {
		return vehicleRepository.countByAgentAgentId(agentId);
	}

}
