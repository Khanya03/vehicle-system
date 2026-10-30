package za.ac.cput.vehiclesystem.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.vehiclesystem.domain.Car;
import za.ac.cput.vehiclesystem.service.CarService;

import java.util.List;

@RestController
@RequestMapping("/api/cars")
public class CarController {

	private final CarService carService;

	public CarController(CarService carService) {
		this.carService = carService;
	}

	@PostMapping
	public ResponseEntity<Car> create(@RequestBody Car car) {
		Car saved = carService.save(car);
		if (saved == null) return ResponseEntity.badRequest().build();
		return ResponseEntity.status(HttpStatus.CREATED).body(saved);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Car> read(@PathVariable String id) {
		Car car = carService.read(id);
		if (car == null) return ResponseEntity.notFound().build();
		return ResponseEntity.ok(car);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Car> update(@PathVariable String id, @RequestBody Car car) {
		if (car == null) return ResponseEntity.badRequest().build();
		if (car.getVehicleId() == null) {
			try {
				java.lang.reflect.Field f = Class.forName("za.ac.cput.vehiclesystem.domain.Vehicle").getDeclaredField("vehicleId");
				f.setAccessible(true);
				f.set(car, id);
			} catch (Exception ignored) {}
		}
		Car updated = carService.update(car);
		if (updated == null) return ResponseEntity.notFound().build();
		return ResponseEntity.ok(updated);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable String id) {
		boolean deleted = carService.delete(id);
		if (!deleted) return ResponseEntity.notFound().build();
		return ResponseEntity.noContent().build();
	}

	@GetMapping
	public ResponseEntity<List<Car>> findAll() {
		List<Car> list = carService.findAll();
		return ResponseEntity.ok(list);
	}

	@GetMapping("/search")
	public ResponseEntity<List<Car>> search(@RequestParam(required = false) String model,
											@RequestParam(required = false) String agentId) {
		if (model != null) {
			return ResponseEntity.ok(carService.findByModel(model));
		}
		if (agentId != null) {
			// CarService doesn't have findByAgentId specifically, but CarRepository has findByAgentId
			// We can reuse findAll and filter, or add repository/service method. For now, return all and let caller filter.
			List<Car> all = carService.findAll();
			List<Car> filtered = all.stream().filter(c -> c.getAgent() != null && c.getAgent().getAgentId().equals(agentId)).toList();
			return ResponseEntity.ok(filtered);
		}
		return ResponseEntity.ok(carService.findAll());
	}
}
