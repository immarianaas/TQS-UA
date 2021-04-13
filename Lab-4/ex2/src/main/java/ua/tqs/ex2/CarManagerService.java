package ua.tqs.ex2;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

public class CarManagerService {

    @Autowired
    CarRepository carRepository;

    public Car save(Car car) {
        carRepository.save(car);
        return car;
    }

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    public Optional<Car> getCarDetails(Long id) {
        Car resp = carRepository.findByCarId(id);
        return resp == null ? null : Optional.of(resp);
    }
}
