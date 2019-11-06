package com.javagda25.securitytemplate.repository;

import com.javagda25.securitytemplate.model.CarRent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRentRepository extends JpaRepository<CarRent, Long> {
}
