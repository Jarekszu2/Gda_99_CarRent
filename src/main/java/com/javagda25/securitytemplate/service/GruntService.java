package com.javagda25.securitytemplate.service;

import com.javagda25.securitytemplate.model.Booking;
import com.javagda25.securitytemplate.model.Car;
import com.javagda25.securitytemplate.model.CarStatus;
import com.javagda25.securitytemplate.repository.BookingRepository;
import com.javagda25.securitytemplate.repository.GruntRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GruntService {

    @Autowired
    private BookingRepository bookingRepository;

    private GruntRepository gruntRepository;

    @Autowired
    public GruntService(GruntRepository carRepository) {
        this.gruntRepository = carRepository;
    }

    public List<Car> getAll() {
        return gruntRepository.findAll();
    }

    public List<Car> getCarsByStatus(CarStatus status) {
        return gruntRepository.findAllByCarStatus(status);
    }

    public void save(Car car) {
        gruntRepository.save(car);
    }

    public Optional<Car> getById(Long carId) {
        return gruntRepository.findById(carId);
    }

    public Car getCarById(Long carId) {
        return gruntRepository.getOne(carId);
    }

    public Page<Car> getPageCars(PageRequest of) {
        return gruntRepository.findAll(of);
    }

    public Page<Booking> getPageBookings(PageRequest of) {
        return bookingRepository.findAll(of);
    }
}