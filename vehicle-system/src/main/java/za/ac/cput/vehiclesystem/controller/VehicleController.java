package za.ac.cput.vehiclesystem.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.vehiclesystem.domain.Vehicle;
import za.ac.cput.vehiclesystem.service.IVehicleService;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

	private final IVehicleService vehicleService;

	public VehicleController(IVehicleService vehicleService) {
		this.vehicleService = vehicleService;
	}

	@PostMapping
	public ResponseEntity<Vehicle> create(@RequestBody Vehicle vehicle) {
		Vehicle saved = vehicleService.save(vehicle);
		if (saved == null) return ResponseEntity.badRequest().build();
		return ResponseEntity.status(HttpStatus.CREATED).body(saved);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Vehicle> read(@PathVariable String id) {
		Vehicle v = vehicleService.read(id);
		if (v == null) return ResponseEntity.notFound().build();
		return ResponseEntity.ok(v);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Vehicle> update(@PathVariable String id, @RequestBody Vehicle vehicle) {
		if (vehicle == null) return ResponseEntity.badRequest().build();
		if (vehicle.getVehicleId() == null) {
			try {
				java.lang.reflect.Field f = Class.forName("za.ac.cput.vehiclesystem.domain.Vehicle").getDeclaredField("vehicleId");
				f.setAccessible(true);
				f.set(vehicle, id);
			} catch (Exception ignored) {}
		}
		Vehicle updated = vehicleService.update(vehicle);
		if (updated == null) return ResponseEntity.notFound().build();
		return ResponseEntity.ok(updated);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable String id) {
		boolean deleted = vehicleService.delete(id);
		if (!deleted) return ResponseEntity.notFound().build();
		return ResponseEntity.noContent().build();
	}

	@GetMapping
	public ResponseEntity<List<Vehicle>> findAll() {
		return ResponseEntity.ok(vehicleService.findAll());
	}

	@GetMapping("/search")
	public ResponseEntity<List<Vehicle>> search(@RequestParam(required = false) String model,
												@RequestParam(required = false) String agentId,
												@RequestParam(required = false) Double minPrice,
												@RequestParam(required = false) Double maxPrice) {
		if (model != null) {
			return ResponseEntity.ok(vehicleService.findByModel(model));
		}
		if (agentId != null) {
			return ResponseEntity.ok(vehicleService.findByAgentId(agentId));
		}
		if (minPrice != null && maxPrice != null) {
			return ResponseEntity.ok(vehicleService.findByModel("")) ; // fallback
		}
		return ResponseEntity.ok(vehicleService.findAll());
	}
}
