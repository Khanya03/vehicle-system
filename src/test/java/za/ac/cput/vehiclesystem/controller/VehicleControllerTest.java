package za.ac.cput.vehiclesystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import za.ac.cput.vehiclesystem.domain.Agent;
import za.ac.cput.vehiclesystem.domain.Car;
import za.ac.cput.vehiclesystem.factory.AgentFactory;
import za.ac.cput.vehiclesystem.factory.NameFactory;
import za.ac.cput.vehiclesystem.service.IVehicleService;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class VehicleControllerTest {

	private MockMvc mockMvc;

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Mock
	private IVehicleService vehicleService;

	@InjectMocks
	private VehicleController vehicleController;

	private Car testCar;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(vehicleController).build();

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
	void create_ShouldReturnCreated() throws Exception {
		when(vehicleService.save(any())).thenReturn(testCar);

		String json = objectMapper.writeValueAsString(testCar);

		mockMvc.perform(post("/api/vehicles")
						.contentType(MediaType.APPLICATION_JSON)
						.content(json))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.vehicleId").value("V001"));
	}

	@Test
	void read_Existing_ShouldReturnVehicle() throws Exception {
		when(vehicleService.read("V001")).thenReturn(testCar);

		mockMvc.perform(get("/api/vehicles/V001"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.model").value("Civic"));
	}

	@Test
	void read_NotFound_ShouldReturn404() throws Exception {
		when(vehicleService.read("V002")).thenReturn(null);

		mockMvc.perform(get("/api/vehicles/V002"))
				.andExpect(status().isNotFound());
	}

	@Test
	void update_ShouldReturnOk() throws Exception {
		when(vehicleService.update(any())).thenReturn(testCar);

		String json = objectMapper.writeValueAsString(testCar);

		mockMvc.perform(put("/api/vehicles/V001")
						.contentType(MediaType.APPLICATION_JSON)
						.content(json))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.vehicleId").value("V001"));
	}

	@Test
	void delete_ShouldReturnNoContent() throws Exception {
		when(vehicleService.delete("V001")).thenReturn(true);

		mockMvc.perform(delete("/api/vehicles/V001"))
				.andExpect(status().isNoContent());

		verify(vehicleService, times(1)).delete("V001");
	}

	@Test
	void findAll_ShouldReturnList() throws Exception {
		when(vehicleService.findAll()).thenReturn(List.of(testCar));

		mockMvc.perform(get("/api/vehicles"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].vehicleId").value("V001"));
	}

	@Test
	void searchByModel_ShouldReturnList() throws Exception {
		when(vehicleService.findByModel("Civic")).thenReturn(List.of(testCar));

		mockMvc.perform(get("/api/vehicles/search").param("model", "Civic"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].vehicleId").value("V001"));
	}

	@Test
	void searchByAgentId_ShouldReturnList() throws Exception {
		when(vehicleService.findByAgentId("AG001")).thenReturn(List.of(testCar));

		mockMvc.perform(get("/api/vehicles/search").param("agentId", "AG001"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].vehicleId").value("V001"));
	}

}