package com.javagda25.securitytemplate.service;

import com.javagda25.securitytemplate.model.Car;
import com.javagda25.securitytemplate.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarService {

    private CarRepository carRepository;

    @Autowired
    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public List<Car> getAll() {
        return carRepository.findAll();
    }

    public void save(Car car) {
        carRepository.save(car);
    }
}
