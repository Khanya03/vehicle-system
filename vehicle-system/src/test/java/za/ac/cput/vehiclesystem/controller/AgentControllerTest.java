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
import za.ac.cput.vehiclesystem.domain.Name;
import za.ac.cput.vehiclesystem.factory.AgentFactory;
import za.ac.cput.vehiclesystem.factory.NameFactory;
import za.ac.cput.vehiclesystem.service.AgentService;

import java.util.List;

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
class AgentControllerTest {

	private MockMvc mockMvc;

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Mock
	private AgentService agentService;

	@InjectMocks
	private AgentController agentController;

	private Agent testAgent;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(agentController).build();

		Name name = NameFactory.createName("Lisa", "Khanya", "Mpahla");
		testAgent = AgentFactory.createAgent("AG001", name, "lisa@gmail.com", "0633292143");
	}

	@Test
	void create_ShouldReturnCreated() throws Exception {
		when(agentService.save(any(Agent.class))).thenReturn(testAgent);

		String json = objectMapper.writeValueAsString(testAgent);

		mockMvc.perform(post("/api/agent")
						.contentType(MediaType.APPLICATION_JSON)
						.content(json))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.agentId").value("AG001"));
	}

	@Test
	void read_Existing_ShouldReturnAgent() throws Exception {
		when(agentService.read("AG001")).thenReturn(testAgent);

		mockMvc.perform(get("/api/agent/AG001"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.email").value("lisa@gmail.com"));
	}

	@Test
	void read_NotFound_ShouldReturn404() throws Exception {
		when(agentService.read("AG002")).thenReturn(null);

		mockMvc.perform(get("/api/agent/AG002"))
				.andExpect(status().isNotFound());
	}

	@Test
	void update_ShouldReturnOk() throws Exception {
		when(agentService.update(any(Agent.class))).thenReturn(testAgent);

		String json = objectMapper.writeValueAsString(testAgent);

		mockMvc.perform(put("/api/agent/AG001")
						.contentType(MediaType.APPLICATION_JSON)
						.content(json))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.agentId").value("AG001"));
	}

	@Test
	void delete_ShouldReturnNoContent() throws Exception {
		when(agentService.delete("AG001")).thenReturn(true);

		mockMvc.perform(delete("/api/agent/AG001"))
				.andExpect(status().isNoContent());

		verify(agentService, times(1)).delete("AG001");
	}

	@Test
	void findAll_ShouldReturnList() throws Exception {
		when(agentService.findAll()).thenReturn(List.of(testAgent));

		mockMvc.perform(get("/api/agent"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].agentId").value("AG001"));
	}

	@Test
	void searchByFirstName_ShouldReturnList() throws Exception {
		when(agentService.findByFirstName("Lisa")).thenReturn(List.of(testAgent));

		mockMvc.perform(get("/api/agent/search").param("firstName", "Lisa"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].agentId").value("AG001"));
	}

}
