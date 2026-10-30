package za.ac.cput.vehiclesystem.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import za.ac.cput.vehiclesystem.domain.Agent;
import za.ac.cput.vehiclesystem.domain.Name;
import za.ac.cput.vehiclesystem.repository.AgentRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AgentServiceTest {

    @Mock
    private AgentRepository agentRepository;

    @InjectMocks
    private AgentService agentService;

    private Agent testAgent;

    @BeforeEach
    void setUp() {
        Name name = new Name.Builder()
                .setFirstName("Lisa")
                .setMiddleName("Khanya")
                .setLastName("Mpahla")
                .build();

        testAgent = new Agent.Builder()
                .setAgentId("AG001")
                .setName(name)
                .setEmail("lisa@gmail.com")
                .setMobile("0633292143")
                .build();
    }

    @Test
    void save_ShouldReturnSavedAgent() {
        when(agentRepository.save(any(Agent.class))).thenReturn(testAgent);

        Agent saved = agentService.save(testAgent);

        assertNotNull(saved);
        assertEquals("AG001", saved.getAgentId());
        verify(agentRepository, times(1)).save(testAgent);
    }

    @Test
    void read_ExistingId_ShouldReturnAgent() {
        when(agentRepository.findById("AG001")).thenReturn(Optional.of(testAgent));

        Agent found = agentService.read("AG001");

        assertNotNull(found);
        assertEquals("lisa@gmail.com", found.getEmail());
        verify(agentRepository, times(1)).findById("AG001");
    }

    @Test
    void read_NonExistingId_ShouldReturnNull() {
        when(agentRepository.findById("AG002")).thenReturn(Optional.empty());

        Agent found = agentService.read("AG002");

        assertNull(found);
        verify(agentRepository, times(1)).findById("AG002");
    }

    @Test
    void update_WhenEntityExists_ShouldSaveAndReturn() {
        when(agentRepository.existsById(testAgent.getAgentId())).thenReturn(true);
        when(agentRepository.save(any(Agent.class))).thenReturn(testAgent);

        Agent updated = agentService.update(testAgent);

        assertNotNull(updated);
        verify(agentRepository, times(1)).existsById(testAgent.getAgentId());
        verify(agentRepository, times(1)).save(testAgent);
    }

    @Test
    void update_WhenEntityDoesNotExist_ShouldReturnNull() {
        when(agentRepository.existsById(testAgent.getAgentId())).thenReturn(false);

        Agent updated = agentService.update(testAgent);

        assertNull(updated);
        verify(agentRepository, times(1)).existsById(testAgent.getAgentId());
        verify(agentRepository, never()).save(any());
    }

    @Test
    void delete_WhenEntityExists_ShouldReturnTrue() {
        when(agentRepository.existsById("AG001")).thenReturn(true).thenReturn(false); // exists before delete, not after

        boolean result = agentService.delete("AG001");

        assertTrue(result);
        verify(agentRepository, times(2)).existsById("AG001");
        verify(agentRepository, times(1)).deleteById("AG001");
    }

    @Test
    void findAll_ShouldReturnList() {
        when(agentRepository.findAll()).thenReturn(List.of(testAgent));

        List<Agent> list = agentService.findAll();

        assertNotNull(list);
        assertEquals(1, list.size());
        verify(agentRepository, times(1)).findAll();
    }

    @Test
    void existsByEmail_ShouldReturnTrueWhenExists() {
        when(agentRepository.existsByEmail("lisa@gmail.com")).thenReturn(true);

        boolean exists = agentService.existsByEmail("lisa@gmail.com");

        assertTrue(exists);
        verify(agentRepository, times(1)).existsByEmail("lisa@gmail.com");
    }
}