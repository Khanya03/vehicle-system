package za.ac.cput.vehiclesystem.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.vehiclesystem.domain.Agent;
import za.ac.cput.vehiclesystem.service.AgentService;

import java.util.List;

@RestController
@RequestMapping("/api/agent")
public class AgentController {

    private final AgentService agentService;

    public AgentController(AgentService agentService) {
        this.agentService = agentService;
    }

    @PostMapping
    public ResponseEntity<Agent> create(@RequestBody Agent agent) {
        Agent saved = agentService.save(agent);
        if (saved == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Agent> read(@PathVariable String id) {
        Agent agent = agentService.read(id);
        if (agent == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(agent);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Agent> update(@PathVariable String id, @RequestBody Agent agent) {
        if (agent == null) return ResponseEntity.badRequest().build();
        if (agent.getAgentId() == null) {
            // ensure id consistency
            // agent id can be set from path if missing
            try {
                java.lang.reflect.Field f = Agent.class.getDeclaredField("agentId");
                f.setAccessible(true);
                f.set(agent, id);
            } catch (Exception ignored) {}
        }
        Agent updated = agentService.update(agent);
        if (updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        boolean deleted = agentService.delete(id);
        if (!deleted) return ResponseEntity.notFound().build();
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Agent>> findAll() {
        List<Agent> list = agentService.findAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Agent>> searchByName(@RequestParam(required = false) String firstName,
                                                    @RequestParam(required = false) String lastName) {
        if (firstName != null) {
            return ResponseEntity.ok(agentService.findByFirstName(firstName));
        }
        if (lastName != null) {
            return ResponseEntity.ok(agentService.findByLastName(lastName));
        }
        return ResponseEntity.ok(agentService.findAll());
    }
}
