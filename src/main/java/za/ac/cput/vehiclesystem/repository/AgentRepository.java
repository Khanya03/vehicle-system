package za.ac.cput.vehiclesystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import za.ac.cput.vehiclesystem.domain.Agent;

import java.util.List;

@Repository
public interface AgentRepository extends JpaRepository<Agent, String> {
    List<Agent> findByNameFirstName(String firstName);
    List<Agent> findByNameLastName(String lastName);
    boolean existsByEmail(String email);
    @Query("SELECT a FROM Agent a WHERE a.name.firstName = :firstName AND a.name.lastName = :lastName")
    List<Agent> findByFirstNameAndLastName(@Param("firstName") String firstName, @Param("lastName") String lastName);
}

