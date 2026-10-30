package za.ac.cput.vehiclesystem.factory;

import za.ac.cput.vehiclesystem.domain.Agent;
import za.ac.cput.vehiclesystem.domain.Name;
import za.ac.cput.vehiclesystem.util.Helper;

public class AgentFactory {
    
    public static Agent createAgent(String agentId, Name name, String email, String mobile) {
        if(!Helper.isNullOrEmpty(agentId) && name != null && Helper.isValidEmail(email) && Helper.isValidMobile(mobile)) {
            return new Agent.Builder()
                    .setAgentId(agentId)
                    .setName(name)
                    .setEmail(email)
                    .setMobile(mobile)
                    .build();
        }
        return null;
    }
}

