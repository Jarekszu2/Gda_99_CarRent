package com.javagda25.securitytemplate.repository;

import com.javagda25.securitytemplate.model.Car;
import com.javagda25.securitytemplate.model.CarStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GruntRepository extends JpaRepository<Car, Long> {
    List<Car> findAllByCarStatus(CarStatus status);
}
