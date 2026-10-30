package za.ac.cput.vehiclesystem.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import za.ac.cput.vehiclesystem.domain.Agent;
import za.ac.cput.vehiclesystem.domain.Car;
import za.ac.cput.vehiclesystem.factory.AgentFactory;
import za.ac.cput.vehiclesystem.factory.NameFactory;
import za.ac.cput.vehiclesystem.repository.VehicleRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VehicleServiceTest {

	@Mock
	private VehicleRepository vehicleRepository;

	@InjectMocks
	private VehicleService vehicleService;

	private Car testCar;

	@BeforeEach
	void setUp() {
		var name = NameFactory.createName("Lisa", "Khanya", "Mpahla");
		Agent agent = AgentFactory.createAgent("AG001", name, "lisa@gmail.com", "0633292143");

		testCar = new Car.Builder()
				.setVehicleId("V001")
				.setModel("Civic")
				.setPrice(20000)
				.setAgent(agent)
				.setNumberOfDoors(4)
				.build();
	}

	@Test
	void save_ShouldReturnSavedVehicle() {
		when(vehicleRepository.save(any(Car.class))).thenReturn(testCar);

		var saved = vehicleService.save(testCar);

		assertNotNull(saved);
		assertEquals("V001", saved.getVehicleId());
		verify(vehicleRepository, times(1)).save(testCar);
	}

	@Test
	void read_ExistingId_ShouldReturnVehicle() {
		when(vehicleRepository.findById("V001")).thenReturn(Optional.of(testCar));

		var found = vehicleService.read("V001");

		assertNotNull(found);
		assertEquals("Civic", found.getModel());
		verify(vehicleRepository, times(1)).findById("V001");
	}

	@Test
	void read_NonExistingId_ShouldReturnNull() {
		when(vehicleRepository.findById("V002")).thenReturn(Optional.empty());

		var found = vehicleService.read("V002");

		assertNull(found);
		verify(vehicleRepository, times(1)).findById("V002");
	}

	@Test
	void update_WhenEntityExists_ShouldSaveAndReturn() {
		when(vehicleRepository.existsById(testCar.getVehicleId())).thenReturn(true);
		when(vehicleRepository.save(any(Car.class))).thenReturn(testCar);

		var updated = vehicleService.update(testCar);

		assertNotNull(updated);
		verify(vehicleRepository, times(1)).existsById(testCar.getVehicleId());
		verify(vehicleRepository, times(1)).save(testCar);
	}

	@Test
	void update_WhenEntityDoesNotExist_ShouldReturnNull() {
		when(vehicleRepository.existsById(testCar.getVehicleId())).thenReturn(false);

		var updated = vehicleService.update(testCar);

		assertNull(updated);
		verify(vehicleRepository, times(1)).existsById(testCar.getVehicleId());
		verify(vehicleRepository, never()).save(any());
	}

	@Test
	void delete_WhenEntityExists_ShouldReturnTrue() {
		when(vehicleRepository.existsById("V001")).thenReturn(true).thenReturn(false);

		boolean result = vehicleService.delete("V001");

		assertTrue(result);
		verify(vehicleRepository, times(1)).existsById("V001");
		verify(vehicleRepository, times(1)).deleteById("V001");
	}

	@Test
	void findAll_ShouldReturnList() {
		when(vehicleRepository.findAll()).thenReturn(List.of(testCar));

		var list = vehicleService.findAll();

		assertNotNull(list);
		assertEquals(1, list.size());
		verify(vehicleRepository, times(1)).findAll();
	}

	@Test
	void findByModel_ShouldReturnMatching() {
		when(vehicleRepository.findByModel("Civic")).thenReturn(List.of(testCar));

		var list = vehicleService.findByModel("Civic");

		assertNotNull(list);
		assertEquals(1, list.size());
		verify(vehicleRepository, times(1)).findByModel("Civic");
	}

	@Test
	void findByAgentId_ShouldReturnMatching() {
		when(vehicleRepository.findByAgentAgentId("AG001")).thenReturn(List.of(testCar));

		var list = vehicleService.findByAgentId("AG001");

		assertNotNull(list);
		assertEquals(1, list.size());
		verify(vehicleRepository, times(1)).findByAgentAgentId("AG001");
	}

	@Test
	void countByAgentId_ShouldReturnCount() {
		when(vehicleRepository.countByAgentAgentId("AG001")).thenReturn(1L);

		long count = vehicleService.countByAgentId("AG001");

		assertEquals(1L, count);
		verify(vehicleRepository, times(1)).countByAgentAgentId("AG001");
	}
}
