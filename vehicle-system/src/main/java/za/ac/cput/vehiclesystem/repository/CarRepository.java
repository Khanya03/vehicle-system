package za.ac.cput.vehiclesystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import za.ac.cput.vehiclesystem.domain.Car;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, String> {
    List<Car> findByModel(String model);
    List<Car> findByPriceBetween(double minPrice, double maxPrice);
    List<Car> findByNumberOfDoors(int numberOfDoors);
    @Query("SELECT c FROM Car c WHERE c.model = :model AND c.numberOfDoors = :doors")
    List<Car> findByModelAndDoors(@Param("model") String model, @Param("doors") int doors);
    @Query("SELECT c FROM Car c WHERE c.agent.agentId = :agentId")
    List<Car> findByAgentId(@Param("agentId") String agentId);
    List<Car> findByPriceGreaterThan(double price);
    List<Car> findByPriceLessThan(double price);
}
