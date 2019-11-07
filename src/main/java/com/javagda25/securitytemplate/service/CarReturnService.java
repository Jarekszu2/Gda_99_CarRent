package com.javagda25.securitytemplate.service;

import com.javagda25.securitytemplate.model.Booking;
import com.javagda25.securitytemplate.model.Car;
import com.javagda25.securitytemplate.model.CarRent;
import com.javagda25.securitytemplate.model.CarReturn;
import com.javagda25.securitytemplate.repository.CarReturnRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;

@Service
public class CarReturnService {

    @Autowired
    private CarReturnRepository carReturnRepository;

    @Autowired
    private CarRentService carRentService;

    public int getDelayPayment(Long idCarRent) {
        CarRent carRent = carRentService.getCarRentById(idCarRent);
        Booking booking = carRent.getBooking();
        LocalDate dateStart = booking.getBookingDate();
        Period period = Period.between(LocalDate.now(), dateStart);
        int days = period.getDays() - booking.getHiresDays();
        if (days <= 0) {
            return 0;
        } else {
            Car car = booking.getCar();
            int feeForDelay = car.getPrice() * 2;
            return days * feeForDelay;
        }
    }

    public LocalDate getDateEnd(Long idCarRent) {
        CarRent carRent = carRentService.getCarRentById(idCarRent);
        Booking booking = carRent.getBooking();
        LocalDate dateStart = booking.getBookingDate();
        LocalDate dateEnd = dateStart.plusDays(booking.getHiresDays());
        return dateEnd;
    }

    public void save(CarReturn carReturn) {
        carReturnRepository.save(carReturn);
    }

    public Page<CarReturn> getPageCarReturnss(PageRequest of) {
        return carReturnRepository.findAll(of);
    }
}
