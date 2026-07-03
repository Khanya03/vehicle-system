package za.ac.cput.vehiclesystem.service;

import org.springframework.stereotype.Service;
import za.ac.cput.vehiclesystem.domain.Agent;
import za.ac.cput.vehiclesystem.repository.AgentRepository;

import java.util.List;

@Service
public class AgentService implements IAgentService {

	private final AgentRepository agentRepository;

	public AgentService(AgentRepository agentRepository) {
		this.agentRepository = agentRepository;
	}

	@Override
	public Agent save(Agent agent) {
		if (agent == null) return null;
		return agentRepository.save(agent);
	}

	@Override
	public Agent read(String agentId) {
		return agentRepository.findById(agentId).orElse(null);
	}

	@Override
	public Agent update(Agent agent) {
		if (agent == null || agent.getAgentId() == null) return null;
		if (!agentRepository.existsById(agent.getAgentId())) return null;
		return agentRepository.save(agent);
	}

	@Override
	public boolean delete(String agentId) {
		if (!agentRepository.existsById(agentId)) return false;
		agentRepository.deleteById(agentId);
		return !agentRepository.existsById(agentId);
	}

	@Override
	public List<Agent> findAll() {
		return agentRepository.findAll();
	}

	@Override
	public List<Agent> findByFirstName(String firstName) {
		return agentRepository.findByNameFirstName(firstName);
	}

	@Override
	public List<Agent> findByLastName(String lastName) {
		return agentRepository.findByNameLastName(lastName);
	}

	@Override
	public boolean existsByEmail(String email) {
		return agentRepository.existsByEmail(email);
	}

}
