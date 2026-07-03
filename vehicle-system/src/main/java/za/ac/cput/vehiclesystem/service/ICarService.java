package za.ac.cput.vehiclesystem.service;

import za.ac.cput.vehiclesystem.domain.Car;
import java.util.List;

public interface ICarService {

    Car save(Car car);

    Car read(String vehicleId);

    Car update(Car car);

    boolean delete(String vehicleId);

    List<Car> findAll();

    List<Car> findByModel(String model);

}
