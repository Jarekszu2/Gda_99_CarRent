package com.javagda25.securitytemplate.service;

import com.javagda25.securitytemplate.model.Booking;
import com.javagda25.securitytemplate.model.Car;
import com.javagda25.securitytemplate.model.CarStatus;
import com.javagda25.securitytemplate.repository.BookingRepository;
import com.javagda25.securitytemplate.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CarService {

    @Autowired
    private BookingRepository bookingRepository;

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

    public Car getCarById(Long carId) {
        return carRepository.getOne(carId);
    }

    public Page<Car> getPageCars(PageRequest of) {
        return carRepository.findAll(of);
    }

    public Page<Car> getPageCarsByStatus(List<String> status, Pageable pageable) {
        List<CarStatus> carStatuses = status.stream().map(CarStatus::valueOf).collect(Collectors.toList());
        return carRepository.findAllByCarStatusIn(carStatuses, pageable);
    }

    public Page<Booking> getNotAcceptedPageBookings(PageRequest of) {
        return bookingRepository.findAllByCarRented( false, of);
    }

    public List<Car> getCarsAVAILABLE() {
        return carRepository.findAllByCarStatus(CarStatus.AVAILABLE);
    }

    public void remove(Long carId) {
        carRepository.deleteById(carId);
    }
}
