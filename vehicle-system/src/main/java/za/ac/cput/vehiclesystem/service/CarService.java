package za.ac.cput.vehiclesystem.service;

import org.springframework.stereotype.Service;
import za.ac.cput.vehiclesystem.domain.Car;
import za.ac.cput.vehiclesystem.repository.CarRepository;

import java.util.List;

@Service
public class CarService implements ICarService {

    private final CarRepository carRepository;

    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @Override
    public Car save(Car car) {
        if (car == null) return null;
        return carRepository.save(car);
    }

    @Override
    public Car read(String vehicleId) {
        return carRepository.findById(vehicleId).orElse(null);
    }

    @Override
    public Car update(Car car) {
        if (car == null || car.getVehicleId() == null) return null;
        if (!carRepository.existsById(car.getVehicleId())) return null;
        return carRepository.save(car);
    }

    @Override
    public boolean delete(String vehicleId) {
        if (!carRepository.existsById(vehicleId)) return false;
        carRepository.deleteById(vehicleId);
        return !carRepository.existsById(vehicleId);
    }

    @Override
    public List<Car> findAll() {
        return carRepository.findAll();
    }

    @Override
    public List<Car> findByModel(String model) {
        return carRepository.findByModel(model);
    }

}