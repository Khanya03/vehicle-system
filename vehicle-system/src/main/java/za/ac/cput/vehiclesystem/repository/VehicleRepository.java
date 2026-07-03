package za.ac.cput.vehiclesystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.ac.cput.vehiclesystem.domain.Vehicle;

import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, String> {
    List<Vehicle> findByModel(String model);
    List<Vehicle> findByPriceBetween(double minPrice, double maxPrice);
    List<Vehicle> findByPriceGreaterThan(double price);
    List<Vehicle> findByPriceLessThan(double price);


    List<Vehicle> findByAgentAgentId(String agentId);
    long countByAgentAgentId(String agentId);

    List<Vehicle> findByAgentNameFirstName(String firstName);
    List<Vehicle> findByAgentNameLastName(String lastName);
}
