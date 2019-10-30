package com.javagda25.securitytemplate.service;

import com.javagda25.securitytemplate.model.Car;
import com.javagda25.securitytemplate.model.CarStatus;
import com.javagda25.securitytemplate.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public List<Car> getCarsByStatus(CarStatus status) {
        return carRepository.findAllByCarStatus(status);
    }

    public void save(Car car) {
        carRepository.save(car);
    }

    public Optional<Car> getById(Long carId) {
        return carRepository.findById(carId);
    }

//    public Page<Author> getPage(PageRequest of) {
//        return authorRepository.findAll(of);
//    }

    public Page<Car> getPage(PageRequest of) {
        return carRepository.findAll(of);
    }
}
