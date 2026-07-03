package za.ac.cput.vehiclesystem.service;

import za.ac.cput.vehiclesystem.domain.Agent;
import java.util.List;

public interface IAgentService {

	Agent save(Agent agent);

	Agent read(String agentId);

	Agent update(Agent agent);

	boolean delete(String agentId);

	List<Agent> findAll();

	List<Agent> findByFirstName(String firstName);

	List<Agent> findByLastName(String lastName);

	boolean existsByEmail(String email);

}
